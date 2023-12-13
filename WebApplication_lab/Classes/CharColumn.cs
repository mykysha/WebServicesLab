using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication.Classes
{
    public class CharColumn : Column
    {
        public CharColumn(string name) : base(name)
        {
            Type = ColumnType.CHAR.ToString();
        }

        public override bool Validate(string data)
        {
            return data.Length == 1 || data.Length == 0;
        }
    }
}
