
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PostgreSQLDAO {
    public List<Motorbike> getBikes() {
        List<Motorbike> bikes = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from motorbikes");
            while (rs.next()) {
                Long id = rs.getLong("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String color = rs.getString("color");
                String fueltank= rs.getString("fueltank");
                String weight = rs.getString("weight");

                Motorbike bike = new Motorbike(id, brand, model, color, fueltank, weight);
                bikes.add(bike);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bikes;
    }

    public List<Motorbike> getFind(Motorbike moto ) {
        List<Motorbike> bikes = new ArrayList<>();
        ArrayList<String> particles = new ArrayList<>();
        int q=0;
        int p;
        try (Connection connection = ConnectionUtil.getConnection()) {
            Statement stmt = connection.createStatement();
            System.out.println("im in getFind");
            String request= "Select * from motorbikes where ";
//            if (moto.getId() == 0 ){
//                p=0;
//                request = createQuery(q,p,request,moto);}
//            else
            p=1;
            request = createQuery(q,p, request, moto);
            request+= ';';
            ResultSet rs = stmt.executeQuery(request);
            System.out.println(request);
            while (rs.next()) {
                Long id = rs.getLong("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String color = rs.getString("color");
                String fueltank= rs.getString("fueltank");
                String weight = rs.getString("weight");
                Motorbike bike = new Motorbike(id,brand,model,color,fueltank,weight);
                bikes.add(bike);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }


        return bikes;
    }
    public List<Motorbike> getFullSearch(Motorbike moto ) {
        List<Motorbike> bikes = new ArrayList<>();
        ArrayList<String> particles = new ArrayList<>();
        int q=0;
        int p;
        try (Connection connection = ConnectionUtil.getConnection()) {
            Statement stmt = connection.createStatement();
            System.out.println("im in getFullFind");
            String request= "Select * from motorbikes where ";

            p=0;
            request = createQuery(q,p,request,moto);
            request+= ';';
            ResultSet rs = stmt.executeQuery(request);
            System.out.println(request);
            while (rs.next()) {
                Long id = rs.getLong("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String color = rs.getString("color");
                String fueltank= rs.getString("fueltank");
                String weight = rs.getString("weight");
                Motorbike bike = new Motorbike(id,brand,model,color,fueltank,weight);
                bikes.add(bike);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bikes;
    }

    public String createQuery( int q, int p, String beginRequest, Motorbike moto){
        ArrayList<String> particles = new ArrayList<>();
        String finalRequest = "";
        switch (p) {
            case 0: {
                if (moto.getId() != 0) break;
                if (moto.getBrand().trim().length() != 0) {
                    particles.add("to_tsvector(brand)");
                }
                if (moto.getModel().trim().length() != 0) {
                    particles.add("to_tsvector(model)");
                }
                if (moto.getColor().trim().length() != 0) {
                    particles.add("to_tsvector(color)");
                }
                if (moto.getFueltank().trim().length() != 0) {
                    particles.add("to_tsvector(fueltank)");
                }
                if (moto.getWeight().trim().length() != 0) {
                    particles.add("to_tsvector(weight)");
                }
                finalRequest = beginRequest + particles.get(0);
                for (int i = 1; i < particles.size(); i++) {
                    finalRequest += " || " + particles.get(i);
                }

                if (moto.getBrand().trim().length() != 0) {
                    finalRequest +=" @@ plainto_tsquery('"+moto.getBrand()+"')";
                    break;
                }
                if (moto.getModel().trim().length() != 0) {
                    finalRequest +=" @@ plainto_tsquery('"+moto.getModel()+"')";
                    break;
                }
                if (moto.getColor().trim().length() != 0) {
                    finalRequest +=" @@ plainto_tsquery('"+moto.getColor()+"')";
                    break;
                }
                if (moto.getFueltank().trim().length() != 0) {
                    finalRequest +=" @@ plainto_tsquery('"+moto.getFueltank()+"')";
                    break;
                }
                if (moto.getWeight().trim().length() != 0) {
                    finalRequest +=" @@ plainto_tsquery('"+moto.getWeight()+"')";
                    break;
                }
                System.out.println("case0: " +finalRequest);
            }


            case 1: {
                if (moto.getId() != 0) particles.add("id=" + moto.getId());
                if (moto.getBrand().trim().length() != 0) {
                    particles.add("brand='" + moto.getBrand() + "'");
                }
                if (moto.getModel().trim().length() != 0) {
                    particles.add("model='" + moto.getModel() + "'");
                }
                if (moto.getColor().trim().length() != 0) {
                    particles.add("color='" + moto.getColor() + "'");
                }
                if (moto.getFueltank().trim().length() != 0) {
                    particles.add("fueltank='" + moto.getFueltank() + "'");
                }
                if (moto.getWeight().trim().length() != 0) {
                    particles.add("weight='" + moto.getWeight() + "'");
                }

                if (q > 0) {
                    finalRequest = beginRequest + particles.get(0);
                    for (int i = 1; i < particles.size(); i++) {
                        finalRequest += " , " + particles.get(i);
                    }
                } else {

                    finalRequest = beginRequest + particles.get(0);
                    for (int i = 1; i < particles.size(); i++) {
                        finalRequest += " and " + particles.get(i);
                    }
                }

            }
        }
        return finalRequest;
    }

    public long create(Motorbike moto){
        long newId=-1;
        System.out.println("im in create");
        try (Connection connection = ConnectionUtil.getConnection()){
            Statement stmt = connection.createStatement();
            String request= "INSERT INTO motorbikes(brand, model, color, fueltank, weight) VALUES ('"+moto.getBrand()+"', '"+
                    moto.getModel()+"', '"+ moto.getColor()+"', '"+moto.getFueltank() +"', '"+moto.getWeight()+"')";
            request += ';';
            int rs1 = stmt.executeUpdate(request);
            Statement stmt1=connection.createStatement();

            ResultSet rs= stmt1.executeQuery("select currval('\"persons_id_seq\"');");
            rs.next();
            newId= rs.getLong("currval");
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newId;
    }

    public String delete(long id) {
        String message="";
        try (Connection connection = ConnectionUtil.getConnection()) {
            Statement stmt = connection.createStatement();
            String request= "DELETE FROM motorbikes WHERE id="+id+';';
            int rs1 = stmt.executeUpdate(request);
            if (rs1>0) message ="Deleted successfully";
            else message="Error";

        }catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }



    public String update(long id, Motorbike moto){
        String message="";
        int q =1;
        int p =1;
        System.out.println("im in Update");
        try (Connection connection = ConnectionUtil.getConnection()) {
            Statement stmt = connection.createStatement();
            String request= "UPDATE motorbikes SET ";
            moto.setId(0);
            request =createQuery(q,p,request,moto);
            request+=" WHERE id="+id+";";
            System.out.println(request);
            int rs1 = stmt.executeUpdate(request);
            if (rs1>0) message ="Updated successfully";
            else message="Error";
        }catch (SQLException ex) {
            Logger.getLogger(PostgreSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
}