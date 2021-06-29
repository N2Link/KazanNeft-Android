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
    public class RockTypesController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/RockTypes
        public IHttpActionResult GetRockTypes()
        {
            List<RockType> ls = new List<RockType>();
            foreach (var item in db.RockTypes)
            {
                ls.Add(new RockType()
                {
                    ID = item.ID,
                    BackgroundColor = item.BackgroundColor,
                    Name = item.Name,
                });
            }
            return Ok(ls);
        }

        // GET: api/RockTypes/5
        [ResponseType(typeof(RockType))]
        public IHttpActionResult GetRockType(long id)
        {
            RockType item = db.RockTypes.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            RockType rockType = new RockType()
            {
                ID = item.ID,
                Name = item.Name,
                BackgroundColor = item.BackgroundColor
            };
            return Ok(rockType);
        }

        // PUT: api/RockTypes/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutRockType(long id, RockType rockType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != rockType.ID)
            {
                return BadRequest();
            }

            db.Entry(rockType).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!RockTypeExists(id))
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

        // POST: api/RockTypes
        [ResponseType(typeof(RockType))]
        public IHttpActionResult PostRockType(RockType rockType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.RockTypes.Add(rockType);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = rockType.ID }, rockType);
        }

        // DELETE: api/RockTypes/5
        [ResponseType(typeof(RockType))]
        public IHttpActionResult DeleteRockType(long id)
        {
            RockType rockType = db.RockTypes.Find(id);
            if (rockType == null)
            {
                return NotFound();
            }

            db.RockTypes.Remove(rockType);
            db.SaveChanges();

            return Ok(rockType);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool RockTypeExists(long id)
        {
            return db.RockTypes.Count(e => e.ID == id) > 0;
        }
    }
}