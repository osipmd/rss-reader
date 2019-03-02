package ru.osipmd.rss;

import ru.osipmd.rss.util.RssUrlHelper;

import java.util.*;

public class RssMain {

    private static final Scanner in = new Scanner(System.in);
    private static final RssHandler rssHandler = new RssHandler();
    private static final String availableParameters = "title, description, pubDate, guid, link, author, category";

    public static void main(String[] args) {
        String command;

        System.out.println("Welcome to simple RSS reader");
        System.out.println("Restoring previously created rss");
        rssHandler.restoreAllRssFromConfig();
        printAvailableCommands();

        while (true) {
            System.out.println("Enter command:");

            command = in.next();

            switch (command) {
                case "add":
                    String addUrl = in.next();
                    addNewRss(addUrl);
                    break;
                case "configure":
                    String configUrl = in.next();
                    configRss(configUrl);
                    break;
                case "turn-on":
                    String turnOnUrl = in.next();
                    rssHandler.turnOnRss(turnOnUrl);
                    break;
                case "turn-off":
                    String turnOffUrl = in.next();
                    rssHandler.turnOffRss(turnOffUrl);
                    break;
                case "on-list":
                    rssHandler.printRunningRss();
                    break;
                case "off-list":
                    rssHandler.printDisabledRss();
                    break;
                case "help":
                    printAvailableCommands();
                    break;
                case "exit":
                    System.out.println("We've saved your configuration for rss channels.");
                    System.out.println("The active channels will be restored at the next launch.");
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command. Print help for get info about available commands...");
            }
        }
    }

    private static void printAvailableCommands() {
        System.out.println("*****Available commands*****");
        System.out.println("\tadd [url] - add new rss chanel");
        System.out.println("\tconfigure [url] - change settings of rss");
        System.out.println("\tturn-on [url] - turn on rss chanel");
        System.out.println("\tturn-off [url] - turn off rss chanel");
        System.out.println("\ton-list - list of running rss");
        System.out.println("\toff-list - list of disabled rss");
        System.out.println("\thelp - get list available commands");
        System.out.println("\texit");
    }

    private static void addNewRss(final String url) {
        System.out.println("Enter output file name. Enter 'default' if you want using default file (file name almost the same as url).");
        String outputFileName = in.next();
        if ("default".equals(outputFileName)) {
            outputFileName = RssUrlHelper.getPathFromUrl(url);
        }
        try {
            System.out.println("Enter poll period in seconds.");
            Long pollPeriod = Long.parseLong(in.next());
            System.out.println("Enter amount of items taken at one time.");
            Long itemsAmount = Long.parseLong(in.next());
            System.out.println("Available for output rss parameters : " + availableParameters);
            System.out.println("Please enter parameters which you want to see in output file. Use ',' as delimiter.");
            in.nextLine();
            List<String> parameters = Arrays.asList(in.nextLine().split(","));
            rssHandler.addRss(url, outputFileName, pollPeriod, itemsAmount, parameters);
            System.out.println("Rss was added");
        } catch (NumberFormatException e) {
            System.out.println("You must enter a number. Please try again.");
        }
    }

    private static void configRss(final String url) {
        System.out.println("Do you want to change output file name? (y/n)");
        String change = in.next();
        if ("y".equals(change.toLowerCase())) {
            changeOutputFileName(url);
            System.out.println("Output file name was changed");
        }
        System.out.println("Do you want to change amount of items? (y/n)");
        change = in.next();
        if ("y".equals(change.toLowerCase())) {
            changeItemsAmount(url);
            System.out.println("Amount of items was changed");
        }
        System.out.println("Do you want to change poll period? (y/n)");
        change = in.next();
        if ("y".equals(change.toLowerCase())) {
            changePollPeriod(url);
        }
    }

    private static void changeOutputFileName(final String url) {
        System.out.println("Enter new output file name:");
        String newOutputFileName = in.next();
        rssHandler.changeOutputFileName(url, newOutputFileName);
        System.out.println("Output file name was changed.");
    }


    private static void changeItemsAmount(final String url) {
        System.out.println("Enter new amount of items:");
        try {
            Long newAmount = Long.parseLong(in.next());
            rssHandler.changeAmountOfItems(url, newAmount);
            System.out.println("Amount of items was changed.");
        } catch (NumberFormatException e) {
            System.out.println("You must enter a number. Please try again.");
        }
    }

    private static void changePollPeriod(final String url) {
        System.out.println("Enter new poll period in seconds:");
        try {
            Long pollPeriod = Long.parseLong(in.next());
            rssHandler.changePollPeriod(url, pollPeriod);
            System.out.println("Poll period was changed.");
        } catch (NumberFormatException e) {
            System.out.println("You must enter a number. Please try again.");
        }
    }
}
