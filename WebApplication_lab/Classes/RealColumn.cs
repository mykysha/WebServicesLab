using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication.Classes
{
    public class RealColumn : Column
    {
        public RealColumn(string name) : base(name)
        {
            Type = ColumnType.REAL.ToString();
        }

        public override bool Validate(string data)
        {
            if (string.IsNullOrEmpty(data))
            {
                return true;
            }

            return double.TryParse(data, out _);
        }
    }
}
