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
    public class WellsController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/Wells
        public IHttpActionResult GetWells()
        {
            List<Well> list = new List<Well>();
            foreach (var item in db.Wells)
            {
                list.Add(new Well()
                {
                    ID = item.ID,
                    WellTypeID = item.WellTypeID,
                    WellName = item.WellName,
                    Capacity = item.Capacity,
                    GasOilDepth = item.GasOilDepth,
                    WellType = new WellType()
                    {
                        ID = item.WellType.ID,
                        Name = item.WellType.Name,
                    }
                });
            }
            return Ok(list);
        }

        // GET: api/Wells/5
        [ResponseType(typeof(Well))]
        public IHttpActionResult GetWell(long id)
        {
            Well item = db.Wells.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            Well well = new Well()
            {
                ID = item.ID,
                WellTypeID = item.WellTypeID,
                WellName = item.WellName,
                Capacity = item.Capacity,
                GasOilDepth = item.GasOilDepth,
                WellType = new WellType()
                {
                    ID = item.WellType.ID,
                    Name = item.WellType.Name,
                }
            };

            return Ok(well) ;
        }

        // PUT: api/Wells/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutWell(long id, Well well)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != well.ID)
            {
                return BadRequest();
            }

            db.Entry(well).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!WellExists(id))
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

        // POST: api/Wells
        [ResponseType(typeof(Well))]
        public IHttpActionResult PostWell(Well well)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Wells.Add(well);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = well.ID }, well);
        }

        // DELETE: api/Wells/5
        [ResponseType(typeof(Well))]
        public IHttpActionResult DeleteWell(long id)
        {
            Well well = db.Wells.Find(id);
            if (well == null)
            {
                return NotFound();
            }

            db.Wells.Remove(well);
            db.SaveChanges();

            return Ok(well);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool WellExists(long id)
        {
            return db.Wells.Count(e => e.ID == id) > 0;
        }
    }
}