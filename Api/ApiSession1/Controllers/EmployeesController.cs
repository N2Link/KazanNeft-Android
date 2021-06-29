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
    public class EmployeesController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/Employees
        public IHttpActionResult GetEmployees()
        {
            List<Employee> list = new List<Employee>();
            foreach (var item in db.Employees)
            {
                list.Add(new Employee()
                {
                    FirstName = item.FirstName,
                    ID = item.ID,
                    LastName = item.LastName,
                    Phone = item.Phone,
                    isAdmin = item.isAdmin,
                    Password = item.Password,
                    Username = item.Username
                });
            }
            return Ok(list);
        }

        // GET: api/Employees/5
        [ResponseType(typeof(Employee))]
        public IHttpActionResult GetEmployee(long id)
        {
            Employee item = db.Employees.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            Employee employee = new Employee()
            {
                FirstName = item.FirstName,
                ID = item.ID,
                LastName = item.LastName,
                Phone = item.Phone,
                isAdmin = item.isAdmin,
                Password = item.Password,
                Username = item.Username
            };
            return Ok(employee);
        }

        // PUT: api/Employees/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutEmployee(long id, Employee employee)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != employee.ID)
            {
                return BadRequest();
            }

            db.Entry(employee).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EmployeeExists(id))
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

        // POST: api/Employees
        [ResponseType(typeof(Employee))]
        public IHttpActionResult PostEmployee(Employee employee)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Employees.Add(employee);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = employee.ID }, employee);
        }

        // DELETE: api/Employees/5
        [ResponseType(typeof(Employee))]
        public IHttpActionResult DeleteEmployee(long id)
        {
            Employee employee = db.Employees.Find(id);
            if (employee == null)
            {
                return NotFound();
            }

            db.Employees.Remove(employee);
            db.SaveChanges();

            return Ok(employee);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool EmployeeExists(long id)
        {
            return db.Employees.Count(e => e.ID == id) > 0;
        }
    }
}