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
    public class QuestionAnnotationController : Controller
    {
        //get access to the database
        private DCTContext db = new DCTContext();



        // GET: QuestionAnnotation/Details/5
        //displays all information in the database related to the  question annaotation
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //finds the question Annotation
            QuestionAnnotation questionAnnotation = db.QuestionAnnotations.Find(id);
            if (questionAnnotation == null)
            {
                return HttpNotFound();
            }
            //returns the detail page with all information on the questionAnnotation displaying
            return View(questionAnnotation);
        }

        // GET: QuestionAnnotation/Create
        //redirects user to a page where they can create annotations
        public ActionResult Create(int projID, string projTitle)
        {
            //sets variables so they can be acccessed in the create view
            ViewBag.ProjectID = projID;
            ViewBag.ProjectTitle = projTitle;
            
            return View();
        }

        // POST: QuestionAnnotation/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        //takes all information supplied by the user and makes a new QuestionAnnotation and adds this to the database
        public ActionResult Create([Bind(Include = "ID,QuestionTitle,RelativeTime,AbsoluteTime,Comment,ProjectID")] QuestionAnnotation questionAnnotation)
        {
            //generates the time of the annotaion from the current system time
            questionAnnotation.AbsoluteTime = DateTime.Now.ToString("HH:mm:ss");
            if (ModelState.IsValid)
            {
                   //save the question Annotation and navigate the user back to the project
                db.QuestionAnnotations.Add(questionAnnotation);
                db.SaveChanges();
                return RedirectToAction("Launch", "Project", new { id = questionAnnotation.ProjectID });
            }

            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", questionAnnotation.ProjectID);
            return View(questionAnnotation);
        }

        // GET: QuestionAnnotation/Edit/5
        //direct the user to a page that allows them to edit an annotation
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //find the annotation in the database
            QuestionAnnotation questionAnnotation = db.QuestionAnnotations.Find(id);
            if (questionAnnotation == null)
            {
                return HttpNotFound();
            }
            //return them the edit view with the fields populated for the particualr annotation
            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", questionAnnotation.ProjectID);
            return View(questionAnnotation);
        }

        // POST: QuestionAnnotation/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.

            //save the changes of the annotation to the database
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "ID,QuestionTitle,RelativeTime,AbsoluteTime,Comment,ProjectID")] QuestionAnnotation questionAnnotation)
        {
            if (ModelState.IsValid)
            {   //save the changes of the annotation to the database and redirect the user back to the project 
                db.Entry(questionAnnotation).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Launch", "Project", new { id = questionAnnotation.ProjectID });
            }
            ViewBag.ProjectID = new SelectList(db.Projects, "ID", "ProjectTitle", questionAnnotation.ProjectID);
            return View(questionAnnotation);
        }

        // GET: QuestionAnnotation/Delete/5
        //populates fields of the annotation so user can review that if this is the annotation they want to delete
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            //find the annotation in the database
            QuestionAnnotation questionAnnotation = db.QuestionAnnotations.Find(id);
            if (questionAnnotation == null)
            {
                return HttpNotFound();
            }
            //return to the view with the populated fields
            return View(questionAnnotation);
        }

        // POST: QuestionAnnotation/Delete/5
        //deletes the annotation
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        
        public ActionResult DeleteConfirmed(int id)
        {
            //finds the annotation in the database and deletes it 
            QuestionAnnotation questionAnnotation = db.QuestionAnnotations.Find(id);
            db.QuestionAnnotations.Remove(questionAnnotation);
            db.SaveChanges();
            //takes the user back to the project
            return RedirectToAction("Launch", "Project", new { id = questionAnnotation.ProjectID });
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
