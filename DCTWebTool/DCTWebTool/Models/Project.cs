using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace DCTWebTool.Models
{
    public class Project
    {
        //primary Key
        public int ID { get; set; }

        [DisplayName("Project Title")]
        [Required(ErrorMessage ="The Project Title requires a value")]
        public string ProjectTitle { get; set; }


        [Required(ErrorMessage = "The Date cannot be left empty")]
        public string Date { get; set; }

        [DisplayName("Audio Path")]
        [Required(ErrorMessage = "The Audio Path requires a value")]
        public string AudioPath { get; set; }

        [DisplayName("Interviewee Details")]
        public string IntervieweeDetails { get; set; }

        //Navigation Properties for accssing tables in the database
        public virtual ICollection<QuestionAnnotation> QuestionAnnotations { get; set; }

        public virtual ICollection<ThemeAnnotation> ThemeAnnotations { get; set; }


    }
}