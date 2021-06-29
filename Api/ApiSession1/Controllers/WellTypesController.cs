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
    public class WellTypesController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/WellTypes
        public IHttpActionResult GetWellTypes()
        {
            List<WellType> ls = new List<WellType>();
            foreach (var item in db.WellTypes)
            {
                ls.Add(new WellType()
                {
                    ID = item.ID,
                    Name = item.Name
                });
            }
            return Ok(ls);
        }

        // GET: api/WellTypes/5
        [ResponseType(typeof(WellType))]
        public IHttpActionResult GetWellType(long id)
        {
            WellType item = db.WellTypes.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            WellType wellType = new WellType()
            {
                ID = item.ID,
                Name = item.Name
            };

            return Ok(wellType);
        }

        // PUT: api/WellTypes/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutWellType(long id, WellType wellType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != wellType.ID)
            {
                return BadRequest();
            }

            db.Entry(wellType).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!WellTypeExists(id))
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

        // POST: api/WellTypes
        [ResponseType(typeof(WellType))]
        public IHttpActionResult PostWellType(WellType wellType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.WellTypes.Add(wellType);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = wellType.ID }, wellType);
        }

        // DELETE: api/WellTypes/5
        [ResponseType(typeof(WellType))]
        public IHttpActionResult DeleteWellType(long id)
        {
            WellType wellType = db.WellTypes.Find(id);
            if (wellType == null)
            {
                return NotFound();
            }

            db.WellTypes.Remove(wellType);
            db.SaveChanges();

            return Ok(wellType);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool WellTypeExists(long id)
        {
            return db.WellTypes.Count(e => e.ID == id) > 0;
        }
    }
}