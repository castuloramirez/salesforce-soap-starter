package com.example;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.fault.ApiFault;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.util.Arrays;

public class SalesforceSoapStarter {

    private static final String LOGIN_URL =  "https://login.salesforce.com/services/Soap/u/38.0";

    public static void main(String[] args) {

        String username;
        String password;

        if (args.length == 2) {
            username = args[0];
            password = args[1];
        }
        else {
            System.out.print("Salesforce Username: ");
            username = System.console().readLine();

            System.out.print("Salesforce Password: ");
            password = new String(System.console().readPassword());
        }

        try {
            final ConnectorConfig loginConfig = new ConnectorConfig();
            loginConfig.setAuthEndpoint(LOGIN_URL);
            loginConfig.setServiceEndpoint(LOGIN_URL);
            loginConfig.setManualLogin(true);

            final PartnerConnection partnerConnection = Connector.newConnection(loginConfig);
            final LoginResult loginResult = partnerConnection.login(username, password);

            partnerConnection.getConfig().setServiceEndpoint(loginResult.getServerUrl());
            partnerConnection.setSessionHeader(loginResult.getSessionId());

            System.out.println("Querying Contacts");
            final QueryResult queryResult = partnerConnection.query("SELECT Id, Name FROM Contact");
            Arrays.stream(queryResult.getRecords()).forEach(System.out::println);
        }
        catch (ApiFault e) {
            System.out.println("Could not connect to Salesforce: " + e.getExceptionMessage());
        }
        catch (ConnectionException e) {
            System.out.println("Could not connect to Salesforce");
            e.printStackTrace();
        }
    }

}
