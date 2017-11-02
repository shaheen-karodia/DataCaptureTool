using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;
using DCTWebTool.Models;


namespace DCTWebTool.DAL
{
    public class DCTInitializer : System.Data.Entity.DropCreateDatabaseIfModelChanges<DCTContext>
    {
        //Everytime the model changes we populate the database with dummy data intially.
        protected override void Seed(DCTContext context)
        {
            var projects = new List<Project>
            {
            new Project{ID=1,ProjectTitle="FirstProject",Date="2016-08-09",AudioPath="/Sound/sample.mp3", IntervieweeDetails="dknkldmldmldcm"},
            new Project{ID=2,ProjectTitle="SecondProject",Date="2014-03-02",AudioPath="/Sound/sample2.mp3", IntervieweeDetails="snaksnlsaclamcalmlacmc"},
             new Project{ID=3,ProjectTitle="ThirdProject",Date="2013-02-09",AudioPath="/Sound/sample3.mp3", IntervieweeDetails="blasdmksadlsamkdlsa"}

            };

            projects.ForEach(s => context.Projects.Add(s));
            context.SaveChanges();



            var themeAnnotations = new List<ThemeAnnotation>
            {
                new ThemeAnnotation{ID=1, ThemeTitle="Greed",RelativeTime=15, AbsoluteTime="2015-02-13",Comment="Some commentary",ProjectID=1},
                new ThemeAnnotation{ID=2,ThemeTitle="Avarice",RelativeTime=25 ,AbsoluteTime ="2015-02-13",Comment="Some commentary",ProjectID=1},
                new ThemeAnnotation{ID=3,ThemeTitle="Envy",RelativeTime=35 ,AbsoluteTime ="2015-02-13",Comment="Some commentary",ProjectID=1},
                new ThemeAnnotation{ID=4,ThemeTitle="Wrath",RelativeTime=45 ,AbsoluteTime ="2015-02-13",Comment="Some commentary",ProjectID=2},
                new ThemeAnnotation{ID=5,ThemeTitle="Sloth",RelativeTime=55 ,AbsoluteTime ="2015-02-13",Comment="Some commentary",ProjectID=2},
                new ThemeAnnotation{ID=6,ThemeTitle="Lust",RelativeTime=65 ,AbsoluteTime ="2015-02-13",Comment="Some commentary",ProjectID=3 }


            };
            themeAnnotations.ForEach(s => context.ThemeAnnotations.Add(s));
            context.SaveChanges();


            var questionAnnotations = new List<QuestionAnnotation>
            {
                new QuestionAnnotation{ID=1, QuestionTitle="Are you not entertained?", RelativeTime=10, AbsoluteTime="2015-02-13", Comment="kancsjnskcnkoa",ProjectID=1},
                new QuestionAnnotation{ID=2,QuestionTitle="What is your deepest fear?", RelativeTime=20 ,AbsoluteTime ="2015-02-13",  Comment="kancsjnskcnkoa",ProjectID=1},
                new QuestionAnnotation{ID=3,QuestionTitle="Who framed Roger Rabbit", RelativeTime=30 ,AbsoluteTime ="2015-02-13",  Comment="kancsjnskcnkoa",ProjectID=1},
                new QuestionAnnotation{ID=4,QuestionTitle="Who moved my cheese?", RelativeTime=40 ,AbsoluteTime ="2015-02-13",  Comment="kancsjnskcnkoa",ProjectID=2},
                new QuestionAnnotation{ID=5,QuestionTitle="How much wood could a Wood Chuck chuck?",RelativeTime=50 ,AbsoluteTime ="2015-02-13",  Comment="kancsjnskcn", ProjectID=2},
                new QuestionAnnotation{ID=6,QuestionTitle="Who let the dogs out?",RelativeTime=60,AbsoluteTime ="2015-02-13", Comment="kancsjnskcnkoa", ProjectID=3},


            };
            questionAnnotations.ForEach(s => context.QuestionAnnotations.Add(s));
            context.SaveChanges();


        
    }

    }



}