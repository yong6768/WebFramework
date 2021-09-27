package framework.dispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.bean.BeanContainer;
import framework.exception.handler.HandlerNotFoundException;
import framework.servlet.handler.HandlerAdapter;
import framework.servlet.handler.HandlerMapping;
import framework.servlet.handler.mvc.view.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DispatcherServlet extends GenericDispatcherServlet {
    public final BeanContainer beanContainer;
    public final ViewResolver viewResolver;

    public List<HandlerMapping> handlerMappings = new ArrayList<>();
    public List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet(BeanContainer beanContainer, ViewResolver viewResolver) {
        this.beanContainer = beanContainer;
        this.viewResolver = viewResolver;
        init();
    }

    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    public void initHandlerMappings() {
        try {
            for (String beanName : beanContainer.getBeanNames()) {
                Object bean = beanContainer.getBean(beanName);
                if(bean instanceof HandlerMapping) {
                    handlerMappings.add((HandlerMapping)bean);
                }
            }
        } catch (Exception e){}
    }

    public void initHandlerAdapters() {
        try {
            for (String beanName : beanContainer.getBeanNames()) {
                Object bean = beanContainer.getBean(beanName);
                if(bean instanceof HandlerAdapter) {
                    handlerAdapters.add((HandlerAdapter) bean);
                }
            }
        } catch (Exception e){}
    }



    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            doDispatch(req, resp);
        } catch (HandlerNotFoundException e) {
            // 404 Error
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {

        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object handler = getHandler(req);
        if(handler == null)
            throw new HandlerNotFoundException();

        HandlerAdapter ha = getHandlerAdapter(handler);

        ModelAndView mv = ha.handle(req, resp, handler);
        if(mv.getViewName() != null) {
            View view = viewResolver.resolveViewName(mv.getViewName());
            view.render(mv.getModel(), req, resp);
        } else {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(mv));
        }
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping hm : handlerMappings) {
            try {
                return hm.getHandler(req);
            } catch (Exception e) {}
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if(handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        return null;
    }
}
