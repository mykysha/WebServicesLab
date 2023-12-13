using System;
using System.Linq;

namespace WebApplication.Classes
{
    public class DatabaseManager
    {
        private static DatabaseManager _instance;
        public static Database Database;

        private DatabaseManager() { }

        public static DatabaseManager GetInstance()
        {
            if (_instance == null)
            {
                _instance = new DatabaseManager();
                Database = new Database("DB");
                _instance.PopulateTable();
                _instance.PopulateTable();
                // Initialize other dependencies if required
            }
            return _instance;
        }

        public void PopulateTable()
        {
            var table = new Table("testTable" + Database.Tables.Count);
            table.AddColumn(new IntegerColumn("column1"));
            table.AddColumn(new RealColumn("column2"));
            table.AddColumn(new StringColumn("column3"));
            table.AddColumn(new CharColumn("column4"));
            table.AddColumn(new ComplexIntegerColumn("column5"));
            table.AddColumn(new ComplexRealColumn("column6"));

            var row1 = new Row();
            row1.Values.Add("10");
            row1.Values.Add("10.0");
            row1.Values.Add("10");
            row1.Values.Add("1");
            row1.Values.Add("10-1i");
            row1.Values.Add("10.50+10i");
            table.AddRow(row1);

            var row2 = new Row();
            row2.Values.Add("15");
            row2.Values.Add("15.0");
            row2.Values.Add("15");
            row2.Values.Add("3");
            row2.Values.Add("15+3i");
            row2.Values.Add("15.7-7i");
            table.AddRow(row2);

            Database.AddTable(table);
        }

        public string RenameDB(string name)
        {
            if (!string.IsNullOrEmpty(name))
            {
                Database.Name = name;
                return name;
            }
            return null;
        }

        public void CreateDB(string name)
        {
            Database = new Database(name);
        }

        public bool AddTable(string name)
        {
            if (!string.IsNullOrEmpty(name))
            {
                var table = new Table(name);
                Database.AddTable(table);
                return true;
            }
            return false;
        }

        public bool DeleteTable(int tableIndex)
        {
            if (tableIndex != -1 && tableIndex < Database.Tables.Count)
            {
                Database.Tables.RemoveAt(tableIndex);
                return true;
            }
            else
            {
                return false;
            }
        }


        public bool AddColumn(int tableIndex, string columnName, ColumnType columnType)
        {
            if (!string.IsNullOrEmpty(columnName) && tableIndex != -1 && tableIndex < Database.Tables.Count)
            {
                Column column = null;
                switch (columnType)
                {
                    case ColumnType.INT:
                        column = new IntegerColumn(columnName);
                        break;
                    case ColumnType.REAL:
                        column = new RealColumn(columnName);
                        break;
                    case ColumnType.STRING:
                        column = new StringColumn(columnName);
                        break;
                    case ColumnType.CHAR:
                        column = new CharColumn(columnName);
                        break;
                    case ColumnType.COMPLEXINT:
                        column = new ComplexIntegerColumn(columnName);
                        break;
                    case ColumnType.COMPLEXREAL:
                        column = new ComplexRealColumn(columnName);
                        break;
                }

                if (column != null)
                {
                    Database.Tables[tableIndex].AddColumn(column);
                    foreach (var row in Database.Tables[tableIndex].Rows)
                    {
                        row.Values.Add("");
                    }
                    return true;
                }
            }
            return false;
        }


        public bool DeleteColumn(int tableIndex, int columnIndex)
        {
            if (columnIndex != -1 && tableIndex != -1 && tableIndex < Database.Tables.Count
                && columnIndex < Database.Tables[tableIndex].Columns.Count)
            {
                Database.Tables[tableIndex].DeleteColumn(columnIndex);
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool AddRow(int tableIndex, Row row)
        {
            if (tableIndex != -1 && tableIndex < Database.Tables.Count)
            {
                var table = Database.Tables[tableIndex];
                for (int i = row.Values.Count; i < table.Columns.Count; i++)
                {
                    row.Values.Add("");
                }
                table.AddRow(row);
                return true;
            }
            return false;
        }

        public bool DeleteRow(int tableIndex, int rowIndex)
        {
            if (rowIndex != -1 && tableIndex != -1 && tableIndex < Database.Tables.Count
                && rowIndex < Database.Tables[tableIndex].Rows.Count)
            {
                Database.Tables[tableIndex].DeleteRow(rowIndex);
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool UpdateCellValue(string value, int tableIndex, int columnIndex, int rowIndex)
        {
            if (tableIndex != -1 && columnIndex != -1 && rowIndex != -1
                && tableIndex < Database.Tables.Count && columnIndex < Database.Tables[tableIndex].Columns.Count
                && rowIndex < Database.Tables[tableIndex].Rows.Count)
            {
                var column = Database.Tables[tableIndex].Columns[columnIndex];
                if (column.Validate(value))
                {
                    Database.Tables[tableIndex].Rows[rowIndex].SetAt(columnIndex, value.Trim());
                    return true;
                }
            }
            return false;
        }

        public bool TablesIntersection(int tableIndex1, int tableIndex2)
        {
            Table tempTable1 = new Table(Database.Tables[tableIndex1]);
            Table tempTable2 = new Table(Database.Tables[tableIndex2]);
            Console.WriteLine(Database.Tables[tableIndex1].Rows[0].Values.Count);
            Console.WriteLine(tempTable1.Rows[0].Values.Count);
            System.Diagnostics.Debug.WriteLine(Database.Tables[tableIndex1].Rows[0].Values.Count);
            System.Diagnostics.Debug.WriteLine(tempTable1.Rows[0].Values.Count);
            int i = 0;
            while (i < tempTable1.Columns.Count)
            {
                bool flag = false;
                int j = i;
                while (j < tempTable2.Columns.Count)
                {
                    if (tempTable1.Columns[i].Name.Equals(tempTable2.Columns[j].Name) &&
                        tempTable1.Columns[i].Type.Equals(tempTable2.Columns[j].Type))
                    {
                        flag = true;
                        Column temp = tempTable2.Columns[i];
                        tempTable2.Columns[i] = tempTable2.Columns[j];
                        tempTable2.Columns[j] = temp;
                        foreach (Row row in tempTable2.Rows)
                        {
                            string data = row.GetAt(i);
                            row.SetAt(i, row.GetAt(j));
                            row.SetAt(j, data);
                        }
                        i++;
                        break;
                    }
                    else
                    {
                        j++;
                    }
                }
                if (!flag)
                {
                    tempTable1.DeleteColumn(i);
                }
            }

            i = 0;
            while (i < tempTable1.Rows.Count)
            {
                bool flag = true;
                int j = 0;
                if (tempTable2.Rows.Count == 0)
                {
                    flag = false;
                }
                while (j < tempTable2.Rows.Count)
                {
                    flag = true;
                    for (int k = 0; k < tempTable1.Rows[i].Values.Count; k++)
                    {
                        System.Diagnostics.Debug.WriteLine(tempTable1.Rows[i].GetAt(k));
                        System.Diagnostics.Debug.WriteLine(tempTable2.Rows[j].GetAt(k));
                        if (!tempTable1.Rows[i].GetAt(k).Equals(tempTable2.Rows[j].GetAt(k)))
                        {
                            flag = false;
                            break;
                        }
                    }
                    if (flag)
                    {
                        System.Diagnostics.Debug.WriteLine("Similar "+i+ " " +j);
                        i++;
                        tempTable2.Rows.RemoveAt(j);
                        break;
                    }
                    else
                    {
                        System.Diagnostics.Debug.WriteLine("Delete j " +j);
                        j++;
                    }
                }
                if (!flag)
                {
                        System.Diagnostics.Debug.WriteLine("Delete i " + i);
                    tempTable1.Rows.RemoveAt(i);
                }
            }
            
            AddTable(tempTable1.Name + " ^ " + tempTable2.Name);
            foreach (Column column in tempTable1.Columns)
            {
                ColumnType columnType = (ColumnType)Enum.Parse(typeof(ColumnType), column.Type);

                AddColumn(Database.Tables.Count - 1, column.Name, columnType);
            }
            System.Diagnostics.Debug.WriteLine(Database.Tables[Database.Tables.Count - 1].Columns.Count);
            foreach (Row row in tempTable1.Rows)
            {
                AddRow(Database.Tables.Count - 1, row);
            }
            System.Diagnostics.Debug.WriteLine(Database.Tables[Database.Tables.Count - 1].Rows.Count);

            return true;
        }
    }
}
