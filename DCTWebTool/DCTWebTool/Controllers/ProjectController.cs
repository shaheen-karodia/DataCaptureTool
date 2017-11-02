using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using DCTWebTool.DAL;
using DCTWebTool.Models;
using Newtonsoft.Json;

namespace DCTWebTool.Controllers
{
    public class ProjectController : Controller
    {
        //accessing the database
        private DCTContext db = new DCTContext();

        // GET: Project
        //lists all the projects in the databas
        public ActionResult Index(string searchString)
        {
            //gets all projects from the database
            var projects = from p in db.Projects
                           select p;

            //filters projects based on the string inputed
            if (!String.IsNullOrEmpty(searchString))
            {
                projects = projects.Where(p => p.ProjectTitle.Contains(searchString));
            }
            //returns a view with all projects listed
            return View(projects.ToList());
        }


        // GET: Project/Create
        //redirects user to a view where they can upload mp3 and json files to create a new project
        public ActionResult CreateUpload()
        {
            return View();
        }

        //takes user inputted files and generates a project from the files
        [HttpPost]
        public ActionResult CreateUpload(IEnumerable<HttpPostedFileBase> files)
        {
            //intialize variables to check if the interview files and project files are uploaded, this is the minimum requirement
            int projID = -1;
            string aPath = "";

            //iterate through all the files
            foreach (var file in files)
            {
    
                if (file != null && file.ContentLength > 0)
                {
                    //check if the file is the interview track
                    if (System.IO.Path.GetExtension(file.FileName) == ".mp3")
                    {
                        //make unique file name and save the file under the sound folder
                        aPath = "/Sound/" + Guid.NewGuid().ToString() + System.IO.Path.GetExtension(file.FileName);
                        file.SaveAs(Server.MapPath("~" + aPath));
                    }
                    else
                    {
                        //only process a project.json file if the interview track was uploaded first
                        if (file.FileName == "project.json" && aPath != "")
                        {
                            //save file and then read it to make a project object
                            file.SaveAs(Server.MapPath("~/App_Data/" + file.FileName));
                            Project proj = JsonConvert.DeserializeObject<Project>(System.IO.File.ReadAllText((Server.MapPath("~/App_Data/" + file.FileName))));
                            proj.AudioPath = aPath;

                            //add the project file to the database and save the changes
                            db.Projects.Add(proj);
                            db.SaveChanges();
                            projID = proj.ID;


                            
                        }
                        else
                        {
                            //only process the files if a project has actually been instantiated already
                            if (file.FileName == "themes.json" && projID != -1)   
                            {
                                //Save the file and make a ThemeAnnotation list
                                file.SaveAs(Server.MapPath("~/App_Data/" + file.FileName));
                                List<ThemeAnnotation> themeAnnotations = JsonConvert.DeserializeObject<List<ThemeAnnotation>>(System.IO.File.ReadAllText((Server.MapPath("~/App_Data/" + file.FileName))));

                                //iterate through each themeAnnotation in the list and add them to the database
                                foreach (var theme in themeAnnotations)
                                {

                                    theme.ProjectID = projID;
                                    db.ThemeAnnotations.Add(theme);

                                }
                                db.SaveChanges();
                            }
                            else
                            {
                                //only process the questions.json file if a project has already been instaniated
                                if (file.FileName == "questions.json" && projID != -1)  
                                {
                                    //SAve the file, read in the Json and make a list of question annotations
                                    file.SaveAs(Server.MapPath("~/App_Data/" + file.FileName));
                                    List<QuestionAnnotation> questionAnnotations = JsonConvert.DeserializeObject<List<QuestionAnnotation>>(System.IO.File.ReadAllText((Server.MapPath("~/App_Data/" + file.FileName))));

                                    //iterate through the list of questionAnnotations and add them to the database
                                    foreach (var q in questionAnnotations)
                                    {

                                        q.ProjectID = projID;
                                        db.QuestionAnnotations.Add(q);

                                    }
                                    db.SaveChanges();
                                }
                            }




                        }


                    }

         
                }
            }

            //redirect to project launch if the project has been successfully made otherwise redirect to the project Index
            if(projID!=-1) //the project ID will only be negative one if a project has not been instantiated
            {
                return RedirectToAction("Launch", "Project", new { id = projID });
            }
            return RedirectToAction("Index", "Project", db.Projects.ToList());
        }












        // GET: Project/Details/5
        //displays all information related to a project
        public ActionResult Details(int? id)
        {
            //ensure user only accesses project with an actual project ID
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //we find the project in the database
            Project project = db.Projects.Find(id);
            if (project == null)
            {
                return HttpNotFound();
            }
            //we display the project information
            return View(project);
        }
        //Launches a project based on the project ID 
        public ActionResult Launch(int? id)
        {
            //only launch projects that have valid ideas
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //find the project in the database
            Project project = db.Projects.Find(id);
            if (project == null)
            {
                return HttpNotFound();
            }
            //launch the project
            return View(project);
        }




  

        // GET: Project/Edit/5
        //User requests to edit a particular project, and the project is retrieed from the database
        public ActionResult Edit(int? id)
        {
            
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //get the project from the database
            Project project = db.Projects.Find(id);
            if (project == null)
            {
                return HttpNotFound();
            }
            //return to a view where users can edit the projects they searched for
            return View(project);
        }

        // POST: Project/Edit/5
        //Submit the Edits made to the project to the database
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "ID,ProjectTitle,Date,AudioPath,IntervieweeDetails")] Project project)
        {
            if (ModelState.IsValid)
            {
                //save the changes to the database and display the  project list to the user
                db.Entry(project).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(project);
        }

        // GET: Project/Delete/5
        //find the project in the database that corresponds to the ID and then delete it from the database
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //finds the project in the database
            Project project = db.Projects.Find(id);
            if (project == null)
            {
                return HttpNotFound();
            }
            //return to the delete page with the information from the particular project that the user wants to delete
            return View(project);
        }

        // POST: Project/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        //finds the project in the database and physically dletes it
        public ActionResult DeleteConfirmed(int id)
        {
            //finds the project in the database and deletes it 
            Project project = db.Projects.Find(id);
            db.Projects.Remove(project);
            db.SaveChanges();
            //returns the user to the view tha lists all the projects 
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
