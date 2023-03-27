package ui;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.*;
import persistence.StoreReader;
import persistence.StoreWriter;
import exceptions.AppDataInvalidException;

// Represents the graphical user interface for Track:GO
public class GraphicalInterface extends JFrame implements ActionListener {
    private static final String DATA_STORE_PATH = "./data/app_data.json";
    private final StoreReader storeReader;
    private final StoreWriter storeWriter;
    private AppData appData;

    JPanel navbar;
    JPanel contentContainer;
    JPanel mainMenu;
    JPanel playersOverviewMenu;
    JPanel rostersOverviewMenu;
    JPanel loadDataMenu;
    JPanel saveDataMenu;

    // EFFECTS: Constructs a new GraphicalInterface
    public GraphicalInterface() {
        super("Track:GO");

        this.storeReader = new StoreReader(DATA_STORE_PATH);
        this.storeWriter = new StoreWriter(DATA_STORE_PATH);

        appData = new AppData();

        setLayout(new BorderLayout());
        instantiatePanels();

        add(navbar, BorderLayout.NORTH);
        add(contentContainer);
        contentContainer.add(mainMenu, "mainMenu");
        contentContainer.add(playersOverviewMenu, "playersOverviewMenu");
        contentContainer.add(rostersOverviewMenu, "rostersOverviewMenu");
        contentContainer.add(loadDataMenu, "loadDataMenu");
        contentContainer.add(saveDataMenu, "saveDataMenu");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(1280, 860));
        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Instantiates JPanels and runs setup methods
    public void instantiatePanels() {
        this.contentContainer = new JPanel(new CardLayout());
        this.navbar =  new JPanel(new GridBagLayout());
        this.mainMenu = new JPanel(new GridBagLayout());
        this.playersOverviewMenu = new JPanel(new GridBagLayout());
        this.rostersOverviewMenu = new JPanel(new GridBagLayout());
        this.loadDataMenu = new JPanel(new GridBagLayout());
        this.saveDataMenu = new JPanel(new GridBagLayout());

        setupNavbar();
        setupMainMenu();
    }

    // REQUIRES: e is not null
    // MODIFIES: this
    // EFFECTS: Handles events
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        CardLayout cl = (CardLayout)(contentContainer.getLayout());

        switch (actionCommand) {
            case "playersOverview":
                setupPlayersOverviewMenu();
                cl.show(contentContainer, "playersOverviewMenu");
                break;
            case "rostersOverview":
                setupRostersOverviewMenu();
                cl.show(contentContainer, "rostersOverviewMenu");
                break;
            case "loadAppData":
                setupLoadConfirmation();
                cl.show(contentContainer, "loadDataMenu");
                break;
            case "saveAppData":
                setupSaveConfirmation();
                cl.show(contentContainer, "saveDataMenu");
                break;
            case "exit":
                System.exit(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: Configures navbar elements
    private void setupNavbar() {
        // Instantiate buttons
        ArrayList<JButton> buttons = new ArrayList<>();
        JButton playersOverviewBtn = new JButton("View Players");
        JButton rostersOverviewBtn = new JButton("View Rosters");
        JButton loadAppDataBtn = new JButton("Load App Data");
        JButton saveAppDataBtn = new JButton("Save App Data");
        JButton exitBtn = new JButton("Exit");

        buttons.add(playersOverviewBtn);
        buttons.add(rostersOverviewBtn);
        buttons.add(loadAppDataBtn);
        buttons.add(saveAppDataBtn);
        buttons.add(exitBtn);

        // Set action commands
        playersOverviewBtn.setActionCommand("playersOverview");
        rostersOverviewBtn.setActionCommand("rostersOverview");
        loadAppDataBtn.setActionCommand("loadAppData");
        saveAppDataBtn.setActionCommand("saveAppData");
        exitBtn.setActionCommand("exit");

        // Final configuration for all buttons on navbar
        allNavbarBtnConfig(buttons);
    }

    // MODIFIES: this
    // EFFECTS: Configures all buttons on navbar
    private void allNavbarBtnConfig(ArrayList<JButton> buttons) {
        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.weightx = 0.5;
        gbConstraints.insets = new Insets(10, 10, 10, 10);

        int gridIndex = 0;

        for (JButton button : buttons) {
            button.addActionListener(this);

            gbConstraints.gridx = gridIndex;
            navbar.add(button, gbConstraints);

            gridIndex++;
        }
    }

    // MODIFIES: this
    // EFFECTS: Configures main menu
    private void setupMainMenu() {
        JLabel centerText1 = new JLabel("Welcome to Track:GO!");
        JLabel centerText2 = new JLabel("Select an option from the navbar to get started.");

        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.fill = GridBagConstraints.VERTICAL;

        gbConstraints.gridy = 0;
        mainMenu.add(centerText1, gbConstraints);

        gbConstraints.gridy = 1;
        gbConstraints.insets = new Insets(15, 0, 0, 0);
        mainMenu.add(centerText2, gbConstraints);
    }

    // MODIFIES: this
    // EFFECTS: Configures players overview menu
    private void setupPlayersOverviewMenu() {
        String[] columnTitles = {"Player", "Games Played", "Games Won", "Games Lost", "Games Tied", "MVPs"};

        ArrayList<Player> playerList = appData.getPlayers();
        Object[][] tableData = new Object[playerList.size()][6];

        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            Object[] playerData = {player.getUsername(), player.getGamesPlayed(), player.getWins(),
                    player.getLosses(), (player.getGamesPlayed() - player.getWins() - player.getLosses()),
                    player.getMostValuablePlayerAwards()};
            tableData[i] = playerData;
        }

        JTable playersTable = new JTable(tableData, columnTitles);

        playersOverviewMenu.add(new JScrollPane(playersTable));
    }

    // MODIFIES: this
    // EFFECTS: Configures rosters overview menu
    private void setupRostersOverviewMenu() {
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.VERTICAL;

        // Add buttons for roster actions on top
        JPanel rostersOverviewMenuButtonPanel = new JPanel(new GridBagLayout());
        setupRostersOverviewMenuButtons(rostersOverviewMenuButtonPanel);
        gbConstraints.gridy = 0;
        rostersOverviewMenu.add(rostersOverviewMenuButtonPanel, gbConstraints);

        // Add rosters overview table
        String[] columnTitles = {"Roster ID", "Win Rate", "Players"};

        ArrayList<Roster> rostersList = appData.getRosters();

        Object[][] tableData = new Object[rostersList.size()][3];

        for (int i = 0; i < rostersList.size(); i++) {
            Roster roster = rostersList.get(i);
            Object[] playerData = {roster.getId(), roster.getWinRate(), roster.getPlayers()};
            tableData[i] = playerData;
        }

        JTable rostersTable = new JTable(tableData, columnTitles);
        PlayerListCellRenderer renderer = new PlayerListCellRenderer();
        rostersTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
        rostersTable.setRowHeight(100);

        gbConstraints.gridy = 1;
        gbConstraints.insets = new Insets(20, 0, 0, 0);
        rostersOverviewMenu.add(new JScrollPane(rostersTable), gbConstraints);
    }

    // MODIFIES: this
    // EFFECTS: Configures rosters overview menu buttons
    private void setupRostersOverviewMenuButtons(JPanel rostersOverviewMenuButtonPanel) {
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;

        JButton playersOverviewBtn = new JButton("Add Player to Roster");

        playersOverviewBtn.addActionListener(this);

        playersOverviewBtn.setActionCommand("addPlayerToRoster");

        gbConstraints.gridx = 0;
        rostersOverviewMenuButtonPanel.add(playersOverviewBtn, gbConstraints);
    }

    // MODIFIES: this
    // EFFECTS: Configures app data load confirmation
    private void setupLoadConfirmation() {
        JLabel centerText1 = new JLabel("Are you sure you want to load app data?");
        JLabel centerText2 = new JLabel("This will overwrite any unsaved data.");
        centerText2.setForeground(Color.RED);
        JButton loadAppDataBtn = new JButton("Load App Data");

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.VERTICAL;

        gbConstraints.gridy = 0;
        loadDataMenu.add(centerText1, gbConstraints);

        gbConstraints.gridy = 1;
        gbConstraints.insets = new Insets(15, 0, 0, 0);
        loadDataMenu.add(centerText2, gbConstraints);

        loadAppDataBtn.addActionListener(e -> handleLoadFromFile());
        gbConstraints.gridy = 2;
        loadDataMenu.add(loadAppDataBtn, gbConstraints);
    }

    // MODIFIES: this
    // EFFECTS: Configures app data save confirmation
    private void setupSaveConfirmation() {
        JLabel centerText1 = new JLabel("Are you sure you want to save the active app data?");
        JLabel centerText2 = new JLabel("This will overwrite your currently saved app data.");
        centerText2.setForeground(Color.RED);
        JButton saveAppDataBtn = new JButton("Save App Data");

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.VERTICAL;

        gbConstraints.gridy = 0;
        saveDataMenu.add(centerText1, gbConstraints);

        gbConstraints.gridy = 1;
        gbConstraints.insets = new Insets(15, 0, 0, 0);
        saveDataMenu.add(centerText2, gbConstraints);

        saveAppDataBtn.addActionListener(e -> handleLoadFromFile());
        gbConstraints.gridy = 2;
        saveDataMenu.add(saveAppDataBtn, gbConstraints);
    }

    // EFFECTS: Handles loading data from file into application
    private void handleLoadFromFile() {
        try {
            storeReader.read(appData);
            System.out.println("Data loaded successfully from " + DATA_STORE_PATH + ".");
        } catch (IOException | AppDataInvalidException e) {
            System.out.println("An error occurred while loading data from " + DATA_STORE_PATH + ": " + e);
        }
    }

    // EFFECTS: Handles saving data to file from application
    private void handleSaveToFile() {
        try {
            storeWriter.open();
            storeWriter.write(appData);
            storeWriter.close();
            System.out.println("Data saved successfully to " + DATA_STORE_PATH + ".");
        } catch (IOException e) {
            System.out.println("An error occurred while saving data to " + DATA_STORE_PATH + ": " + e);
        }
    }
}