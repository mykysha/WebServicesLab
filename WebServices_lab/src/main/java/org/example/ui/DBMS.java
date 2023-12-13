package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.component.Column;
import org.example.component.Row;
import org.example.component.TableData;
import org.example.component.column.ColumnType;
import org.example.generated.WebService;
import org.example.generated.WebServiceSoap;
import org.example.parser.ColumnDeserializer;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DBMS {

    private static DBMS instance;
    private static String databaseName = "Відкрийте або створіть базу данних";
    JFrame frame;
    public static JTabbedPane tabbedPane;
    public JMenuBar menuBar;
    public JMenuItem deleteTableMenuItem;
    public JMenuItem addRowMenuItem;
    public JMenuItem addColumnMenuItem;
    public JMenuItem deleteRowMenuItem;
    public JMenuItem deleteColumnMenuItem;
    public JMenuItem createTableMenuItem;
    public JMenuItem tablesIntersection;

    public JMenu tableMenu = new JMenu("Таблиця");
    public JMenu columnMenu = new JMenu("Колонка");
    public JMenu rowMenu = new JMenu("Рядок");

    public JLabel databaseLabel;
    static WebServiceSoap webServiceSoap;

    public static DBMS getInstance(){
        if (instance == null){
            instance = new DBMS();

            instance.frame = new JFrame("DBMS");
            instance.menuBar = new JMenuBar();
            instance.tabbedPane = new JTabbedPane();
            instance.deleteTableMenuItem = new JMenuItem("Видалити");
            instance.addRowMenuItem = new JMenuItem("Додати");
            instance.addColumnMenuItem = new JMenuItem("Додати");
            instance.deleteRowMenuItem = new JMenuItem("Видалити");
            instance.deleteColumnMenuItem = new JMenuItem("Видалити");
            instance.createTableMenuItem = new JMenuItem("Створити");
            instance.tablesIntersection = new JMenuItem("Перетин таблиць");

            WebService stub = new WebService();
            webServiceSoap = stub.getWebServiceSoap();
        }
        return instance;
    }

    public static void main(String[] args) {
        DBMS dbms = DBMS.getInstance();

        dbms.tableMenu.add(dbms.createTableMenuItem);
        dbms.tableMenu.add(dbms.deleteTableMenuItem);
        dbms.tableMenu.add(dbms.tablesIntersection);

        dbms.columnMenu.add(dbms.addColumnMenuItem);
        dbms.columnMenu.add(dbms.deleteColumnMenuItem);

        dbms.rowMenu.add(dbms.addRowMenuItem);
        dbms.rowMenu.add(dbms.deleteRowMenuItem);

        dbms.menuBar.add(dbms.tableMenu);
        dbms.menuBar.add(dbms.columnMenu);
        dbms.menuBar.add(dbms.rowMenu);

        dbms.databaseLabel = new JLabel(databaseName, SwingConstants.CENTER);
        dbms.frame.setSize(800, 600);
        dbms.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dbms.frame.setJMenuBar(DBMS.instance.menuBar);
        dbms.frame.getContentPane().add(DBMS.getInstance().tabbedPane, BorderLayout.CENTER);
        dbms.frame.getContentPane().add(dbms.databaseLabel, BorderLayout.NORTH);
        dbms.frame.setLocationRelativeTo(null);
        dbms.frame.setVisible(true);


        dbms.createTableMenuItem.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog(dbms.frame, "Введіть назву нової таблиці:");
            boolean result = webServiceSoap.createTable(tableName);
            if (result) {
                addTable(tableName);
            } else {
                System.out.println("Table creation Error: " + tableName);
            }
        });

        dbms.deleteTableMenuItem.addActionListener(e -> {
            int selectedIndex = dbms.tabbedPane.getSelectedIndex();
            boolean result = webServiceSoap.deleteTable(selectedIndex);
            if (result) {
                tabbedPane.removeTabAt(selectedIndex);
            } else {
                System.out.println("Table delete Error: " + selectedIndex);
            }
        });


        dbms.addColumnMenuItem.addActionListener(e -> {
            boolean result = false;
            int selectedTab = dbms.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                String newColumnName = JOptionPane.showInputDialog(dbms.frame, "Введіть назву нової колонки:");

                if (newColumnName != null && !newColumnName.isEmpty()) {
                    ColumnType selectedDataType = (ColumnType) JOptionPane.showInputDialog(
                            dbms.frame,
                            "Оберіть тип нової колонки:",
                            "Додати Колонку",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            ColumnType.values(),
                            ColumnType.INT
                    );

                    if (selectedDataType != null) {
                        result = webServiceSoap.addColumn(selectedTab, newColumnName, org.example.generated.ColumnType.fromValue(selectedDataType.name()));
                        if (result){
                            addColumn(selectedTab, newColumnName, selectedDataType.name());
                        } else {
                            System.out.println("Add Column Error: tab" + selectedTab);
                        }

                    }
                }
            }
        });

        dbms.deleteColumnMenuItem.addActionListener(e -> {
            int selectedTab = dbms.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) dbms.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                int selectedColumn = table.getSelectedColumn();
                boolean result = webServiceSoap.deleteColumn(selectedTab, selectedColumn);
                if(result){
                    tableModel.removeColumn(selectedColumn);
                } else {
                    System.out.println("Delete Column Error: tab" + selectedTab);
                }
            }
        });

        dbms.addRowMenuItem.addActionListener(e -> {
            int selectedTab = dbms.tabbedPane.getSelectedIndex();
            boolean result = webServiceSoap.addRow(selectedTab);
            if(result) {
                JPanel tablePanel = (JPanel) tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();
                tableModel.addRow(new Object[tableModel.getColumnCount()]);
            } else {
                System.out.println("Add Row Error: tab" + selectedTab);
            }
        });

        dbms.deleteRowMenuItem.addActionListener(e -> {
            int selectedTab = dbms.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) dbms.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                int selectedRow = table.getSelectedRow();
                boolean result = webServiceSoap.deleteRow(selectedTab, selectedRow);
                if(result) {
                    tableModel.removeRow(selectedRow);
                } else {
                    System.out.println("Delete Row Error: tab" + selectedTab);
                }
            }
        });


        dbms.tablesIntersection.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            List<TableData> selectorTables = new ArrayList<>();
            List<TableData> tableData = getTableData();

            for (int i = 0; i < tableData.size(); i++){
                if (i != selectedTab) selectorTables.add(tableData.get(i));
            }
            List<String> tableNames = new ArrayList<>();
            for (TableData table : selectorTables) {
                tableNames.add(table.name);
            }
            String[] tableNamesArray = tableNames.toArray(new String[0]);
            String tableName = (String) JOptionPane.showInputDialog(
                    instance.frame,
                    "Choose second table for intersection:",
                    "Tables intersection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    tableNamesArray,
                    tableNamesArray[0]
            );

            int intersectTable = -1;
            for (int i = 0; i < tableData.size(); i++){
                if (tableData.get(i).name.equals(tableName)) intersectTable = i;
            }

            if (selectedTab != -1 && intersectTable != -1) {
                boolean response = webServiceSoap.tablesIntersection(selectedTab, intersectTable);
                if (response) {
                    List<TableData> tableData1 = getTableData();
                    addTable(tableData1.get(tableData1.size()-1).name);
                    List<Column> columns = getColumn(tableData1.size()-1);
                    for (int i = 0; i < columns.size(); i++) {
                        addColumn(tableData1.size()-1,columns.get(i).name,columns.get(i).getType());
                    }
                    JPanel tablePanel = (JPanel) dbms.tabbedPane.getComponentAt(tableData1.size()-1);
                    JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                    JTable table = (JTable) scrollPane.getViewport().getView();
                    CustomTableModel tableModel = (CustomTableModel) table.getModel();
                    List<Row> rows = getRows(tableData1.size()-1);
                    for (int i = 0; i < rows.size(); i++) {
                            Object[] rowData = new Object[columns.size()];
                            for (int i1 = 0; i1 < rowData.length; i1++) {
                                rowData[i1] = rows.get(i).values.get(i1);
                            }
                            tableModel.addRow(rowData);
                    }
                    JOptionPane.showMessageDialog(DBMS.instance.frame, "Intersection found!");
                }
            }
        });

//
//        dbms.tablesIntersection.addActionListener(e -> {
//            int selectedTab = dbms.tabbedPane.getSelectedIndex();
//            if (selectedTab != -1) {
//                JPanel tablePanel = (JPanel) dbms.tabbedPane.getComponentAt(selectedTab);
//                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
//                JTable table = (JTable) scrollPane.getViewport().getView();
//                CustomTableModel tableModel = (CustomTableModel) table.getModel();
//
//                boolean result = webServiceSoap.deleteDuplicateRows(selectedTab);
//                if (result){
//                    System.out.println(tableModel.getRowCount());
//                    int count = tableModel.getRowCount();
//                    for (int i = 0; i < count;  i++) {
//                        tableModel.removeRow(0);
//                    }
//                    System.out.println(tableModel.getRowCount());
//                    ObjectMapper mapper = new ObjectMapper();
//                    String rowsResult = webServiceSoap.getRows(selectedTab);
//                    try {
//                        List<Row> rows = mapper.readValue(rowsResult, new TypeReference<List<Row>>(){});
//                        for (int i = 0; i < rows.size(); i++) {
//                            Object[] rowData = new Object[tableModel.getColumnCount()];
//                            for (int i1 = 0; i1 < rowData.length; i1++) {
//                                rowData[i1] = rows.get(i).values.get(i1);
//                            }
//                            tableModel.addRow(rowData);
//                        }
//                        JOptionPane.showMessageDialog(DBMS.instance.frame, "Вітаю! Дублікати рядків видалено");
//                    } catch (JsonProcessingException ex) {
//                        System.out.println("JsonProcessingException" + ex);
//                    }
//                } else {
//                    System.out.println("Delete Row Duplicates Error: tab" + selectedTab);
//                }
//            }
//        });

        updateDB();
    }
    private static void addColumn(int selectedTab, String newColumnName, String selectedDataType) {
        JPanel tablePanel = (JPanel) tabbedPane.getComponentAt(selectedTab);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        JTable table = (JTable) scrollPane.getViewport().getView();
        CustomTableModel tableModel = (CustomTableModel) table.getModel();

        tableModel.addColumn(newColumnName + " (" + selectedDataType + ")");
    }

    public static void updateDB(){
        clearTables();
        List<TableData> tableData = getTableData();
        for (int tableIndex = 0; tableIndex < tableData.size(); tableIndex++) {
            addTable(tableData.get(tableIndex).name);

            List<Column> columns = getColumn(tableIndex);
            for (int i1 = 0; i1 < columns.size(); i1++) {
                Column column = columns.get(i1);
                addColumn(tableIndex, column.name, column.type);
            }
            JPanel tablePanel = (JPanel) tabbedPane.getComponentAt(tableIndex);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            JTable table = (JTable) scrollPane.getViewport().getView();
            CustomTableModel tableModel = (CustomTableModel) table.getModel();
            List<Row> rows = getRows(tableIndex);
            for (int i = 0; i < rows.size(); i++) {
                Object[] rowData = new Object[columns.size()];
                for (int i1 = 0; i1 < rowData.length; i1++) {
                    rowData[i1] = rows.get(i).values.get(i1);
                }
                tableModel.addRow(rowData);
            }
        }
    }

    private static void clearTables() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.removeTabAt(0);
        }
    }

    public static JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();

        CustomTable table = new CustomTable(model);

        DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());

        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn = table.getSelectedColumn();
                Object updatedValue = table.getValueAt(selectedRow, selectedColumn);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellEditor(cellEditor);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        CustomTableModel tableModel = new CustomTableModel ();

        table.setModel(tableModel);

        return panel;
    }

    public static void addTable(String name){
        if (name != null && !name.isEmpty()) {
            tabbedPane.addTab(name, createTablePanel());
        }
    }

    public static List<Row> getRows(int tableIndex){
        ObjectMapper mapper = new ObjectMapper();
        String result = webServiceSoap.getRows(tableIndex);
        List<Row> rows = new ArrayList<>();
        try {
            rows = mapper.readValue(result, new TypeReference<List<Row>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static List<Column> getColumn(int tableIndex){
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Column.class, new ColumnDeserializer());
        mapper.registerModule(module);

        String result = webServiceSoap.getColumns(tableIndex);
        List<Column> columns = new ArrayList<>();
        try {
            columns = mapper.readValue(result, new TypeReference<List<Column>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }

    public static List<TableData> getTableData(){
        ObjectMapper mapper = new ObjectMapper();
        String result = webServiceSoap.getTablesData();
        List<TableData> tableData = new ArrayList<>();
        try {
            tableData = mapper.readValue(result, new TypeReference<List<TableData>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return tableData;
    }

}