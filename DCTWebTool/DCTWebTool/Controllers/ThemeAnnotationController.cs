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

namespace DCTWebTool.Controllers
{
    public class ThemeAnnotationController : Controller
    {
        //gets access to the database
        private DCTContext db = new DCTContext();



        // GET: ThemeAnnotation/Details/5
        //directs user to page where they can view details of a particular theme
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //finds the themeAnnotation in the database
            ThemeAnnotation themeAnnotation = db.ThemeAnnotations.Find(id);
            if (themeAnnotation == null)
            {
                return HttpNotFound();
            }
            //returns user to a place where they can view the details of the theme
            return View(themeAnnotation);
        }

        // GET: ThemeAnnotation/Create
        //directs user to a view where they can create an annoation
        public ActionResult Create(int projID, string projTitle)
        {
            //intialize variables that the create view requires
            ViewBag.ProjectID = projID;
            ViewBag.ProjectTitle = projTitle;
            return View();
        }

        // POST: ThemeAnnotation/Create
        //creates a annotation from information inputted from the user
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "ID,ThemeTitle,RelativeTime,AbsoluteTime,Comment,ProjectID")] ThemeAnnotation themeAnnotation)
        {
            if (ModelState.IsValid)
            {
                //generate the absolute time from the current system time, add the annotation to the database and redirect user to project launch
                themeAnnotation.AbsoluteTime=DateTime.Now.ToString("HH:mm:ss");
                db.ThemeAnnotations.Add(themeAnnotation);
                db.SaveChanges();
                return RedirectToAction("Launch", "Project", new { id = themeAnnotation.ProjectID });
            }

            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", themeAnnotation.ProjectID);
            return View(themeAnnotation);
        }

        // GET: ThemeAnnotation/Edit/5
        //directs user to view where they can edit the information of an annotation
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //find the annotation in the database
            ThemeAnnotation themeAnnotation = db.ThemeAnnotations.Find(id);
            if (themeAnnotation == null)
            {
                return HttpNotFound();
            }
            //return the view with the annotation information
            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", themeAnnotation.ProjectID);
            return View(themeAnnotation);
        }

        // POST: ThemeAnnotation/Edit/5
        //adding an annotation to the database based on the information inputted by the user.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "ID,ThemeTitle,RelativeTime,AbsoluteTime,Comment,ProjectID")] ThemeAnnotation themeAnnotation)
        {
            if (ModelState.IsValid)
            {
                //save changes to the database and take user to the project launch page
                db.Entry(themeAnnotation).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Launch", "Project", new { id = themeAnnotation.ProjectID });
            }
            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", themeAnnotation.ProjectID);
            return View(themeAnnotation);
        }

        // GET: ThemeAnnotation/Delete/5
        //directs user to a page where they can delete an annotation
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //retrieves annotation fromthe database 
            ThemeAnnotation themeAnnotation = db.ThemeAnnotations.Find(id);
            if (themeAnnotation == null)
            {
                return HttpNotFound();
            }
            //returns to the delete page with information on the annotation to be deleted
            return View(themeAnnotation);
        }

        // POST: ThemeAnnotation/Delete/5
        //deletes the annotation from the database
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            //locates annotation in the database and removes it from the the database
            ThemeAnnotation themeAnnotation = db.ThemeAnnotations.Find(id);
            db.ThemeAnnotations.Remove(themeAnnotation);
            db.SaveChanges();
            return RedirectToAction("Launch", "Project", new { id = themeAnnotation.ProjectID });
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
