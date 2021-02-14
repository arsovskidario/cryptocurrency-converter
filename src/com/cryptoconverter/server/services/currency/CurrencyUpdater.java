package com.cryptoconverter.server.services.currency;

import com.cryptoconverter.server.services.exceptions.CurrencyNotPresentException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyUpdater {

    private static final String API_KEY = " D33079E6-2806-4A88-864D-9B0EF95709A4";
    private static final String HOST_NAME = "www.coinapi.io";
    private static final String HTTP_REQUEST = "GET /v1/assets/";

    static Map<String, Currency> nameToCurrency = new HashMap<>();


    private static Gson gson = new Gson();

    /**
     * Initializes  the DB  with the price of the top 50 crypto currencies
     */
    public static void initializeCurrencies() {
        try {
            List<CurrencyData> currencyData = sendRequest();
            for (CurrencyData data : currencyData) {
                nameToCurrency.put(data.getAsset_id(),
                        new Currency(data.getAsset_id(), data.getName(), data.getPrice_usd()));
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            System.out.println("Error while sending request to COINAPI" + e);
        }
    }

    /**
     * Sends request to API to get actual data of currencies and updates them in the DB
     */
    public static void updateCurrencies() {
        try {
            List<CurrencyData> currencyData = sendRequest();
            for (CurrencyData data : currencyData) {
                Currency currency = nameToCurrency.get(data.getAsset_id());
                if (currency != null) {
                    currency.updatePrice(data.getPrice_usd());
                }
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            System.out.println("Error while sending request to COINAPI" + e);
        }
    }

    /**
     * Send request to API and get actual price of 50 crypto currencies.
     *
     * @return list containing cryptocurrencies data
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */

    private static List<CurrencyData> sendRequest() throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder()
                .header("X-CoinAPI-Key", API_KEY)
                .uri(URI.create("https://rest.coinapi.io/v1/assets"))
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());


        Type type = new TypeToken<List<CurrencyData>>() {
        }.getType();
        List<CurrencyData> data = gson.fromJson(response.body(), type);

        return data.stream()
                .filter(CurrencyData::isCrypto)
                .collect(Collectors.toList());

    }

    public static Currency getCurrency(String name) {
        Currency result = nameToCurrency.get(name);
        if (result == null) {
            throw new CurrencyNotPresentException();
        }

        return result;
    }


    public static List<Currency> getAvailableCurrencies() {
        return List.copyOf(nameToCurrency.values());
    }
}
