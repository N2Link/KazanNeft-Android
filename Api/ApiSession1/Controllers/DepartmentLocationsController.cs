using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using ApiSession1;

namespace ApiSession1.Controllers
{
    public class DepartmentLocationsController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/DepartmentLocations
        public IHttpActionResult GetDepartmentLocations()
        {
            List<DepartmentLocation> list = new List<DepartmentLocation>();
            foreach (var item in db.DepartmentLocations)
            {
                list.Add(new DepartmentLocation()
                {
                    ID = item.ID,
                    DepartmentID = item.DepartmentID,
                    EndDate = item.EndDate,
                    LocationID = item.LocationID,
                    StartDate = item.StartDate,
                    Department = new Department()
                    {
                        ID = item.DepartmentID,
                        Name = item.Department.Name,
                    },
                    Location = new Location()
                    {
                        ID = item.LocationID,
                        Name = item.Location.Name,
                    }
                });
            }
            return Ok(list);
        }

        // GET: api/DepartmentLocations/5
        [ResponseType(typeof(DepartmentLocation))]
        public IHttpActionResult GetDepartmentLocation(long id)
        {
            DepartmentLocation item = db.DepartmentLocations.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            DepartmentLocation departmentLocation = new DepartmentLocation()
            {
                ID = item.ID,
                DepartmentID = item.DepartmentID,
                EndDate = item.EndDate,
                LocationID = item.LocationID,
                StartDate = item.StartDate,
                Department = new Department()
                {
                    ID = item.DepartmentID,
                    Name = item.Department.Name,
                },
                Location = new Location()
                {
                    ID = item.LocationID,
                    Name = item.Location.Name,
                }
            };

            return Ok(departmentLocation);
        }

        // PUT: api/DepartmentLocations/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutDepartmentLocation(long id, DepartmentLocation departmentLocation)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != departmentLocation.ID)
            {
                return BadRequest();
            }

            db.Entry(departmentLocation).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DepartmentLocationExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/DepartmentLocations
        [ResponseType(typeof(DepartmentLocation))]
        public IHttpActionResult PostDepartmentLocation(DepartmentLocation departmentLocation)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.DepartmentLocations.Add(departmentLocation);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = departmentLocation.ID }, departmentLocation);
        }

        // DELETE: api/DepartmentLocations/5
        [ResponseType(typeof(DepartmentLocation))]
        public IHttpActionResult DeleteDepartmentLocation(long id)
        {
            DepartmentLocation departmentLocation = db.DepartmentLocations.Find(id);
            if (departmentLocation == null)
            {
                return NotFound();
            }

            db.DepartmentLocations.Remove(departmentLocation);
            db.SaveChanges();

            return Ok(departmentLocation);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool DepartmentLocationExists(long id)
        {
            return db.DepartmentLocations.Count(e => e.ID == id) > 0;
        }
    }
}