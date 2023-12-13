using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication.Classes
{
    public class IntegerColumn : Column
    {
        public IntegerColumn(string name) : base(name)
        {
            Type = ColumnType.INT.ToString();
        }

        public override bool Validate(string data)
        {
            if (string.IsNullOrEmpty(data))
            {
                return true;
            }

            return int.TryParse(data, out _);
        }
    }
}