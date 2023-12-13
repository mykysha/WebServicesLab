using System;
using System.Globalization;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Text.RegularExpressions;

namespace WebApplication.Classes
{
    public class ComplexRealColumn : Column
    {
        public ComplexRealColumn(string name) : base(name)
        {
            this.Type = ColumnType.COMPLEXREAL.ToString();
        }

        public override bool Validate(string data)
        {
            // Define a regular expression pattern for complex numbers
            string complexIntegerPattern = @"^(0|[-]?0(\.\d+)|[-]?[1-9]\d*(\.\d+)?)([-+](0(\.\d+)?|[1-9]\d*(\.\d+)?))i$";
            Regex regex = new Regex(complexIntegerPattern);
            Match match = regex.Match(data);

            // Check if the input data matches the pattern
            return match.Success || string.IsNullOrEmpty(data);
        }
    }
}
