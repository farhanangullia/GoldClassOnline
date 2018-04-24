package web.filter;

import entity.StaffEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})

public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/BackOfficeAdminSystem";

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (isLogin && requestServletPath.equals("/index.xhtml")) {
            StaffEntity currentStaffEntity = (StaffEntity) httpSession.getAttribute("currentStaffEntity");
            if (currentStaffEntity.getAccessRightEnum().equals(AccessRightEnum.ADMIN)) {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/adminMainPage.xhtml");
            } else if (currentStaffEntity.getAccessRightEnum().equals(AccessRightEnum.CINEMASTAFF)) {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/cinemaManagement.xhtml");
            }
        }

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                StaffEntity currentStaffEntity = (StaffEntity) httpSession.getAttribute("currentStaffEntity");

                if (checkAccessRight(requestServletPath, currentStaffEntity.getAccessRightEnum())) {
                    chain.doFilter(request, response);
                } else {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/error.xhtml");
                }
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/index.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private Boolean checkAccessRight(String path, AccessRightEnum accessRight) {
        if (accessRight.equals(AccessRightEnum.ADMIN)) {
            if (path.equals("/adminMainPage.xhtml")
                    || path.equals("/cinemaManagement.xhtml")
                    || path.equals("/movieManagement.xhtml")
                    || path.equals("/viewCinemaDetails.xhtml")
                    || path.equals("/viewHallDetails.xhtml")
                    || path.equals("/screeningSchedule.xhtml")) {
                return true;
            } else {
                return false;
            }
        } else if (accessRight.equals(AccessRightEnum.CINEMASTAFF)) {
            if (path.equals("/cinemaManagement.xhtml")
                    || path.equals("/movieManagement.xhtml")
                    || path.equals("/viewCinemaDetails.xhtml")
                    || path.equals("/viewHallDetails.xhtml")
                    || path.equals("/screeningSchedule.xhtml")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/error.xhtml")
                || path.startsWith("/images")
                || path.startsWith("/javax.faces.resource")) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean checkAccessRight(String path) {
        if (path.startsWith("/userPortal")) {
            return true;
        } else {
            String accessRight = path.replaceAll(".xhtml", "");
            accessRight = accessRight.substring(1);

            return true;
        }
    }
}
