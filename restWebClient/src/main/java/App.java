import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.Null;
import javax.ws.rs.core.MediaType;

import static com.sun.xml.internal.ws.api.message.Packet.State.ClientResponse;

public class App {
    private static String URL = "http://localhost:8080/rest/motorbike";
    private static String pathFullSearch = "/search";
    private static String pathGetFind = "/find";
    private static String pathMoto ="/%d";


    public static void main(String[] args) {

        Client client = Client.create();


        Motorbike moto = new Motorbike() ;
        long id;
        String answer;
        String message;
        Scanner in = new Scanner(System.in);

        System.out.println("Do you want to change the database?(yes/no)");
        answer = in.nextLine();
        System.out.println(answer);
        while(answer.equals("yes")) {
            System.out.println("select the action: findAll,find,fullSearching,create,update, delete");
            String act = in.nextLine();

            switch (act) {
                case "findAll":{
                    printList(getMotorbikes(client));
                    break;
                }
                case "find": {

                  //  printList(getMotorbikes(client));
                    System.out.println("Enter id:");
                    moto.setId(in.nextLong());

                    System.out.println("Enter brand:");
                    moto.setBrand(in.nextLine());

                    System.out.println("Enter model:");
                    moto.setModel(in.nextLine());

                    System.out.println("Enter color: ");
                    moto.setColor(in.nextLine());

                    System.out.println("Enter fueltank: ");
                    moto.setFueltank(in.nextLine());

                    System.out.println("Enter weight: ");
                    moto.setWeight(in.nextLine());

                    printList(getFindMotorbikes(client, moto));
                    break;
                }
                case "fullSearching":{

                    System.out.println("Enter brand:");
                    moto.setBrand(in.nextLine());

                    System.out.println("Enter model:");
                    moto.setModel(in.nextLine());

                    System.out.println("Enter color: ");
                    moto.setColor(in.nextLine());

                    System.out.println("Enter fueltank: ");
                    moto.setFueltank(in.nextLine());

                    System.out.println("Enter weight: ");
                    moto.setWeight(in.nextLine());
                    printList(getFullSearch(client,moto));
                    break;
                }
                case "create":{
                    System.out.println("Enter brand:");
                    moto.setBrand(in.nextLine());

                    System.out.println("Enter model:");
                    moto.setModel(in.nextLine());

                    System.out.println("Enter color: ");
                    moto.setColor(in.nextLine());

                    System.out.println("Enter fueltank: ");
                    moto.setFueltank(in.nextLine());

                    System.out.println("Enter weight: ");
                    moto.setWeight(in.nextLine());
                    create(client,moto);
                    break;
                }
                case "update":{

                    System.out.println("Enter id:");
                    id = in.nextLong();

                    System.out.println("Enter brand:");
                    moto.setBrand(in.nextLine());

                    System.out.println("Enter model:");
                    moto.setModel(in.nextLine());

                    System.out.println("Enter color: ");
                    moto.setColor(in.nextLine());

                    System.out.println("Enter fueltank: ");
                    moto.setFueltank(in.nextLine());

                    System.out.println("Enter weight: ");
                    moto.setWeight(in.nextLine());

                    message =update(client, id , moto);
                    System.out.println(message);
                    break;
                }
                case "delete": {

                        System.out.println("Enter id:");
                        id = in.nextLong();
                        message = delete(client,id);
                        System.out.println(message);
                        break;

                }
            }

            System.out.println("What do you want to search on database? (yes/no)");
            answer = in.nextLine();
        }

    }




    private static List<Motorbike> getMotorbikes(Client client) {
        WebResource webResource = client.resource(URL);
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        GenericType<List<Motorbike>> type = new GenericType<List<Motorbike>>() {};
        return response.getEntity(type);
    }
    private static List<Motorbike> getFindMotorbikes(Client client, Motorbike motorb){
        String URLGetFind = URL+ pathGetFind;
        WebResource webResource = client.resource(URLGetFind);
        if (motorb.getId()!= 0) {webResource = webResource.queryParam("id", String.valueOf(motorb.getId()));}
        if (motorb.getBrand().trim().length()!=0) {webResource =webResource.queryParam("brand",motorb.getBrand());}
        if (motorb.getModel().trim().length()!=0) {webResource =webResource.queryParam("model",motorb.getModel());}
        if (motorb.getColor().trim().length()!=0) {webResource =webResource.queryParam("color",motorb.getColor());}
        if (motorb.getFueltank().trim().length()!=0) {webResource =webResource.queryParam("fueltank",motorb.getFueltank());}
        if (motorb.getWeight().trim().length()!=0) {webResource =webResource.queryParam("weight",motorb.getWeight());}

        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        GenericType<List<Motorbike>> type = new GenericType<List<Motorbike>>() {};
        return response.getEntity(type);

    }

    private static List<Motorbike> getFullSearch(Client client, Motorbike motorb) {
        String  URLSearch = URL + pathFullSearch;
        WebResource webResource = client.resource(URLSearch);
        if (motorb.getBrand().trim().length()!=0) {webResource =webResource.queryParam("brand",motorb.getBrand());}
        if (motorb.getModel().trim().length()!=0) {webResource =webResource.queryParam("model",motorb.getModel());}
        if (motorb.getColor().trim().length()!=0) {webResource =webResource.queryParam("color",motorb.getColor());}
        if (motorb.getFueltank().trim().length()!=0) {webResource =webResource.queryParam("fueltank",motorb.getFueltank());}
        if (motorb.getWeight().trim().length()!=0) {webResource =webResource.queryParam("weight",motorb.getWeight());}

        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        GenericType<List<Motorbike>> type = new GenericType<List<Motorbike>>() {};
        return response.getEntity(type);
    }

    private static long create(Client client, Motorbike moto) {

        WebResource webResource = client.resource(URL);

        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, moto);
        GenericType<Long> type = new GenericType<Long>() {};

        return response.getEntity(type);
    }
    private static String update(Client client, long id, Motorbike moto) {
        String formStr = String.format(pathMoto, id);
        String URLUpdate = URL + formStr;
        WebResource webResource = client.resource(URLUpdate);
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, moto);
        GenericType<String> type = new GenericType<String>() {
        };

        return response.getEntity(type);
    }

    private static String delete(Client client,long id) {
        String formStr = String.format(pathMoto, id);
        String URLDelete = URL + formStr;
        WebResource webResource = client.resource(URLDelete);
        ClientResponse response =
                webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        GenericType<String> type = new GenericType<String>() {
        };

        return response.getEntity(type);
    }

    private static   void printList(List<Motorbike> Motorbikes) {
        for (Motorbike bike : Motorbikes) {
            System.out.println(bike);
        }
    }
}