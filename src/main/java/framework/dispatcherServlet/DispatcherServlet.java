package framework.dispatcherServlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DispatcherServlet extends GenericDispatcherServlet {

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("DispatcherServlet called");
        log.info(req.getRequestURI());
        resp.getWriter().println("Hello World");
    }
}
