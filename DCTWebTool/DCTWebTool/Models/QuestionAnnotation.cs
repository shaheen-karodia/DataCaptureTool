using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using System.ComponentModel;

namespace DCTWebTool.Models
{
    public class QuestionAnnotation
    {
       //primary Key
        public int ID { get; set; }

        [DisplayName("Question Title")]
        [Required(ErrorMessage = "The Question Title requires a value")]
        public string QuestionTitle { get; set; }

        [DisplayName("Relative Time (Secs)")]
        
        [Required(ErrorMessage = "The Relative time requires a value")]
        [Range(0, int.MaxValue, ErrorMessage = "Please enter a positive integer")]
        public int RelativeTime { get; set; }

        [DisplayName("Absolute Time")]

        [Required(ErrorMessage = "The Absolute time requires a value")]
        
        public string AbsoluteTime { get; set; }

        public string Comment { get; set; }
        
        //foreign key for the database
        public int ProjectID { get; set; }

        //navigation properties to store tables from the database
        public virtual Project Project { get; set; }

    }
}