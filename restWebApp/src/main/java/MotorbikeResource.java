

import com.sun.jersey.core.spi.factory.ResponseImpl;
import errors.IllegalNameException;
import errors.IllegalNameExceptionMapper;
import jdk.nashorn.internal.objects.annotations.Getter;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/motorbike")
@Produces({MediaType.APPLICATION_JSON})
public class MotorbikeResource {

    @GET
    public List<Motorbike> getBikes(){
        List<Motorbike> motorbikes;
        Motorbike motorb = new Motorbike();
        motorbikes = new PostgreSQLDAO().getBikes();

        for (Motorbike bike : motorbikes) {
        System.out.println(bike);
    }
        return motorbikes;
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Motorbike> getFind(@QueryParam("id") long id,@QueryParam("brand") String brand,
                                  @QueryParam("model") String model,@QueryParam("color") String color,
                                  @QueryParam("fueltank") String fueltank, @QueryParam("weight") String weight) throws
            IllegalNameException {
        if ((id ==0) && ((brand == null )|| ( brand.trim().isEmpty())) && (model == null || model.trim().isEmpty())&&
                (color == null || color.trim().isEmpty()) && (fueltank == null || fueltank.trim().isEmpty()) &&
                (weight== null || weight.trim().isEmpty())) {
            throw IllegalNameException.DEFAULT_INSTANCE;
        }
        List<Motorbike> motorbikes;
        Motorbike motorb = new Motorbike();

            if (brand==null){brand="";}
            if (model==null){model="";}
            if (color ==null){color="";}
            if (fueltank==null){fueltank="";}
            if (weight==null){weight="";}
            motorb.setId(id);
            motorb.setBrand(brand);
            motorb.setModel(model);
            motorb.setColor(color);
            motorb.setFueltank(fueltank);
            motorb.setWeight(weight);
            motorbikes = new PostgreSQLDAO().getFind(motorb);

        for (Motorbike bike : motorbikes) {
            System.out.println(bike);
        }
        return motorbikes;
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Motorbike> getFullTextFind(@QueryParam("id") long id,@QueryParam("brand") String brand,
                                   @QueryParam("model") String model,@QueryParam("color") String color,
                                   @QueryParam("fueltank") String fueltank, @QueryParam("weight") String weight) throws IllegalNameException {
        List<Motorbike> motorbikes;
        Motorbike motorb = new Motorbike();
        if ((id ==0) && ((brand == null )|| ( brand.trim().isEmpty())) && (model == null || model.trim().isEmpty())&&
                (color == null || color.trim().isEmpty()) && (fueltank == null || fueltank.trim().isEmpty()) &&
                (weight== null || weight.trim().isEmpty())) {
            throw IllegalNameException.DEFAULT_INSTANCE;
        }

            if (brand==null){brand="";}
            if (model==null){model="";}
            if (color ==null){color="";}
            if (fueltank==null){fueltank="";}
            if (weight==null){weight="";}
            motorb.setId(id);
            motorb.setBrand(brand);
            motorb.setModel(model);
            motorb.setColor(color);
            motorb.setFueltank(fueltank);
            motorb.setWeight(weight);
            motorbikes = new PostgreSQLDAO().getFullSearch(motorb);


        for (Motorbike bike : motorbikes) {
            System.out.println(bike);
        }
        return motorbikes;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  Response create (Motorbike moto) throws IllegalNameException {
        if ((moto.getBrand() == null || moto.getBrand().trim().isEmpty()) || (moto.getModel() == null || moto.getModel().trim().isEmpty()||
                (moto.getColor() == null || moto.getColor() .trim().isEmpty()) || (moto.getFueltank() == null || moto.getFueltank().trim().isEmpty())||
                (moto.getWeight()== null || moto.getWeight().trim().isEmpty()))) {
            throw new IllegalNameException("Error: The value of one of the fields is not entered!");
        }
        PostgreSQLDAO dao = new PostgreSQLDAO();

        return Response.status(Response.Status.CREATED).entity(dao.create(moto)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Motorbike moto) throws IllegalNameException {
        PostgreSQLDAO dao = new PostgreSQLDAO();
        List<Motorbike> bikes = new ArrayList<>();
        moto.setId(id); moto.setBrand(""); moto.setModel("");
        moto.setColor(""); moto.setFueltank(""); moto.setWeight("");
        bikes = dao.getFind(moto);

        if(bikes.isEmpty()){
            Response response =new IllegalNameExceptionMapper().toResponse( new IllegalNameException("Error: Field didn't found with this Id !"));
            return response;
        }
        else {
            return Response.status(Response.Status.ACCEPTED).entity(dao.update(id, moto)).build();
        }
}

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        PostgreSQLDAO dao = new PostgreSQLDAO();
        Motorbike moto = new Motorbike();
        List<Motorbike> bikes = new ArrayList<>();
        moto.setId(id); moto.setBrand(""); moto.setModel("");
        moto.setColor(""); moto.setFueltank(""); moto.setWeight("");
        bikes = dao.getFind(moto);
        if(bikes.isEmpty()){
            Response response =new IllegalNameExceptionMapper().toResponse( new IllegalNameException("Error: Field didn't found with this Id !"));
            return response;
        }
        else {
            return Response.status(Response.Status.ACCEPTED).entity(dao.delete(id)).build();
        }
    }
}