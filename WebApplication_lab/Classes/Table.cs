using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication.Classes
{
    public class Table
    {
        public string Name { get; set; }
        public List<Row> Rows { get; } = new List<Row>();
        public List<Column> Columns { get; } = new List<Column>();

        public Table(string name)
        {
            Name = name;
        }

        // Copy constructor
        public Table(Table table)
        {
            Name = table.Name;
            foreach (Row row in table.Rows)
            {
                Row newRow = new Row();
                foreach (string data in row.Values)
                {
                    newRow.Values.Add(data);
                }
                Rows.Add(newRow);
            }
            foreach (Column column in table.Columns)
            {
                Column newColumn;
                ColumnType columnType = (ColumnType)Enum.Parse(typeof(ColumnType), column.Type);
                switch (columnType)
                {
                    case ColumnType.INT:
                        newColumn = new IntegerColumn(column.Name);
                        break;
                    case ColumnType.REAL:
                        newColumn = new RealColumn(column.Name);
                        break;
                    case ColumnType.STRING:
                        newColumn = new StringColumn(column.Name);
                        break;
                    case ColumnType.CHAR:
                        newColumn = new CharColumn(column.Name);
                        break;
                    case ColumnType.COMPLEXINT:
                        newColumn = new ComplexIntegerColumn(column.Name);
                        break;
                    case ColumnType.COMPLEXREAL:
                        newColumn = new ComplexRealColumn(column.Name);
                        break;
                    default:
                        throw new InvalidOperationException("Unknown column type");
                }
                Columns.Add(newColumn);
            }
        }

        public void AddRow(Row row)
        {
            Rows.Add(row);
        }

        public void DeleteRow(int rowIndex)
        {
            Rows.RemoveAt(rowIndex);
        }

        public void DeleteColumn(int columnIndex)
        {
            Columns.RemoveAt(columnIndex);
            foreach (Row row in Rows)
            {
                row.Values.RemoveAt(columnIndex);
            }
        }

        public void AddColumn(Column column)
        {
            Columns.Add(column);
        }
    }
}
