import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ViewBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        PrintWriter out = res.getWriter();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cafe_db", "root", "");

            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM bookings ORDER BY id DESC");

            StringBuilder json = new StringBuilder();
            json.append("[");

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }
                first = false;

                // 🔥 NULL SAFE VALUES
                String table = rs.getString("table_no");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String name = rs.getString("name");
                int guests = rs.getInt("guests");

                String food = rs.getString("food");
                String status = rs.getString("food_status");

                if (food == null || food.equals(""))
                    food = "Not Ordered";
                if (status == null || status.equals(""))
                    status = "Pending";

                json.append("{");
json.append("\"table\":\"").append(table).append("\",");
json.append("\"date\":\"").append(date).append("\",");
json.append("\"time\":\"").append(time).append("\",");
json.append("\"name\":\"").append(name).append("\",");
json.append("\"guests\":\"").append(guests).append("\",");


json.append("\"food\":\"").append(food).append("\",");
json.append("\"status\":\"").append(status).append("\"");

json.append("}");
            }

            json.append("]");

            out.print(json.toString());

        } catch (Exception e) {

            e.printStackTrace(); // 🔥 console में दिखेगा

            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");

        } finally {

            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
            }

            if (out != null)
                out.close();
        }
    }
}