using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;

namespace WebApplication.Classes
{
    public class ComplexIntegerColumn : Column
    {
        public ComplexIntegerColumn(string name) : base(name)
        {
            this.Type = ColumnType.COMPLEXINT.ToString();
        }

        public override bool Validate(string data)
        {
            // Define a regular expression pattern for complex numbers
            string complexIntegerPattern = @"^(0|[-]?[1-9]\d*)([-+](0|[1-9]\d*))i$";
            Regex regex = new Regex(complexIntegerPattern);
            Match match = regex.Match(data);

            // Check if the input data matches the pattern
            return match.Success || string.IsNullOrEmpty(data);
        }
    }
}
