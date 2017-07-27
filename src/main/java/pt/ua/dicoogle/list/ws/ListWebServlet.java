/**
 * Copyright (C) 2017 UA.PT Bioinformatics - http://bioinformatics.ua.pt/
 *
 * This file is part of Dicoogle/list.
 *
 * Dicoogle/list is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dicoogle/list is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Dicoogle.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ua.dicoogle.list.ws;

import net.sf.json.JSONObject;
import org.json.JSONException;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.dicoogle.list.DicoogleFileLister;
import pt.ua.dicoogle.sdk.StorageInputStream;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

/** Main web service.
 *
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
public class ListWebServlet  extends HttpServlet implements PlatformCommunicatorInterface {
    private static final Logger logger = LoggerFactory.getLogger(ListWebServlet.class);
    
    private DicooglePlatformInterface platform;
    private DicoogleFileLister lister;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
        String qsUri = req.getParameter("uri");
        String qsDepth = req.getParameter("depth");
        String qsPSize = req.getParameter("psize");
        String qsPOffset = req.getParameter("poffset");

        if (qsUri == null) {
            JSONObject reply = new JSONObject();
            reply.put("error", "no content");
            resp.getWriter().print(reply.toString());
            resp.setStatus(400);
            return;
        }

        int depth = 0;
        if (qsDepth != null) {
            try {
                depth = Integer.parseInt(qsDepth);
            } catch (NumberFormatException ex) {
                JSONObject reply = new JSONObject();
                reply.put("error", "illegal depth parameter");
                resp.getWriter().print(reply.toString());
                resp.setStatus(400);
            }
        }

        int psize = -1;
        if (qsPSize != null) {
            try {
                psize = Integer.parseInt(qsPSize);
            } catch (NumberFormatException ex) {
                JSONObject reply = new JSONObject();
                reply.put("error", "illegal psize parameter");
                resp.getWriter().print(reply.toString());
                resp.setStatus(400);
            }
        }

        int offset = 0;
        if (qsPOffset != null) {
            try {
                offset = Integer.parseInt(qsPOffset);
            } catch (NumberFormatException ex) {
                JSONObject reply = new JSONObject();
                reply.put("error", "illegal poffset parameter");
                resp.getWriter().print(reply.toString());
                resp.setStatus(400);
            }
        }

        final URI u;

        try {
            u = new URI(qsUri);
        } catch (URISyntaxException e) {
            JSONObject reply = new JSONObject();
            reply.put("error", "invalid URI");
            resp.getWriter().print(reply.toString());
            resp.setStatus(400);
            return;
        }

        Stream<StorageInputStream> files = this.lister.filesIn(u, depth);

        if (offset > 0) {
            files = files.skip(offset);
        }
        if (psize > 0) {
            files = files.limit(psize);
        }

        resp.setContentType("application/json");
        resp.setStatus(200);

        JSONWriter writer = new JSONWriter(resp.getWriter());
        try {
            writer.object()
                    .key("root").value(u.toString());
            if (offset > 0) {
                writer.key("offset").value(offset);
            }
            if (psize > 0) {
                writer.key("page-size").value(psize);
            }
            writer.key("files").array();

            files.map(f -> f.getURI().toString())
                 .forEachOrdered(uri -> {
                     try {
                         writer.value(uri);
                     } catch (JSONException e) {
                         logger.error("JSON serialization error", e);
                     }
                 });

            writer.endArray()
                    .endObject();
        } catch (JSONException e) {
            resp.setStatus(500);
            logger.error("JSON serialization error", e);
        }
    }

    @Override
    public void setPlatformProxy(DicooglePlatformInterface core) {
        this.platform = core;
        this.lister = new DicoogleFileLister(this.platform);
    }
}
