package com.distsys.geoip;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

/**
 *
 * @author murilotuvani
 */
@WebServlet(name = "GeoipServlet", urlPatterns = { "/" }, initParams = {
		@WebInitParam(name = "cliente", value = "localhost") })
public class GeoipServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4423175207649831931L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			/* TODO output your page here. You may use following sample code. */
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet NewServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
			String remoteAddress = request.getParameter("ip");
			if (remoteAddress == null) {
				remoteAddress = request.getRemoteAddr();
			}

			out.println("Let's find out the location for IP " + remoteAddress);

			File database = new File("GeoLite2-City.mmdb");
			if (database.exists()) {
				out.print("<p>Found file " + database.getAbsolutePath());
				out.println("</p>");

				DatabaseReader reader = new DatabaseReader.Builder(database).build();

				InetAddress ipAddress = InetAddress.getByName(remoteAddress);

				// Replace "city" with the appropriate method for your database, e.g.,
				// "country".
				CityResponse cityResponse = reader.city(ipAddress);

				Country country = cityResponse.getCountry();
				out.println("<ul>");
				if (country != null) {
					out.println("<li>Contry code : " + country.getIsoCode() + "</li>");
					out.println("<li>Contry name : " + country.getNames() + "</li>");
					System.out.println(country.getIsoCode()); // 'US'
					System.out.println(country.getName()); // 'United States'
					System.out.println(country.getNames().get("zh-CN"));
				}

				Subdivision subdivision = cityResponse.getMostSpecificSubdivision();
				if (subdivision != null) {
					out.println("<li>Subdivision name : " + subdivision.getName() + "</li>");
					out.println("<li>Subdivision code : " + subdivision.getIsoCode() + "</li>");
					System.out.println(subdivision.getName()); // 'Minnesota'
					System.out.println(subdivision.getIsoCode()); // 'MN'
				}

				City city = cityResponse.getCity();
				if (city != null) {
					out.println("<li>City : " + city.getName() + "</li>");
				}

				Postal postal = cityResponse.getPostal();
				if (postal != null) {
					out.println("<li>Postal code : " + postal.getCode() + "</li>");
					System.out.println(postal.getCode()); // '55455'
				}

				Location location = cityResponse.getLocation();
				if (location != null) {
					out.println("<li>Latide    : " + location.getLatitude() + "</li>");
					out.println("<li>Longitude : " + location.getLongitude() + "</li>");
					System.out.println(location.getLatitude()); // 44.9733
					System.out.println(location.getLongitude()); // -93.2323
				}

				out.println("</ul>");

			} else {
				out.print("<p>Couldn't find file " + database.getAbsolutePath());
				out.println("</p>");
			}

			out.println("</body>");
			out.println("</html>");
		} catch (GeoIp2Exception e) {
			out.print("<p>Sorry we're having a bad time here, please come up latter</p>");
			out.println("</body>");
			out.println("</html>");
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
	// + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
