using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace DCTWebTool.Models
{
    public class ThemeAnnotation
    {
        //Primary Key
        public int ID { get; set; }

        [DisplayName("Theme Title")]
        [Required(ErrorMessage = "The Theme Title requires a value")]
        public string ThemeTitle { get; set; }

        [DisplayName("Relative Time (Secs)")]
        [Required(ErrorMessage = "The Relative time requires a value")]
        [Range(0, int.MaxValue, ErrorMessage = "Please enter positive integer")]
        public int RelativeTime { get; set; }

        [DisplayName("Absolute Time")]
        [Required(ErrorMessage = "The Absolute time requires a value")]
        public string AbsoluteTime { get; set; }

        public string Comment { get; set; }

        //foreign key in the Database
        public int ProjectID { get; set; }

        //navigation property to access project in the database
        public virtual Project Project { get; set; }


 


    }
}