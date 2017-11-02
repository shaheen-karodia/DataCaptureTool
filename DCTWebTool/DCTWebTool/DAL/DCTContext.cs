using DCTWebTool.Models;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;

namespace DCTWebTool.DAL
{
    public class DCTContext : DbContext
    {
        //connect to the database
        public DCTContext() : base("DCTContext")
        {
        }
        //get all the sets from the database
        public DbSet<Project> Projects { get; set; }
        public DbSet<QuestionAnnotation> QuestionAnnotations { get; set; }
        public DbSet<ThemeAnnotation> ThemeAnnotations { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();
        }
    }
}