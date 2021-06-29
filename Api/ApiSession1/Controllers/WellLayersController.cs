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
    public class WellLayersController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/WellLayers
        public IHttpActionResult GetWellLayers()
        {
            List<WellLayer> ls = new List<WellLayer>();
            foreach (var item in db.WellLayers)
            {
                ls.Add(new WellLayer()
                {
                    ID = item.ID,
                    EndPoint = item.EndPoint,
                    RockTypeID = item.RockTypeID,
                    StartPoint = item.StartPoint,
                    WellID = item.WellID,
                    RockType = new RockType()
                    {
                        ID = item.RockTypeID,
                        BackgroundColor = item.RockType.BackgroundColor,
                        Name = item.RockType.Name
                    },
                    Well = new Well()
                    {
                        ID = item.Well.ID,
                        Capacity = item.Well.Capacity,
                        GasOilDepth = item.Well.GasOilDepth,
                        WellName = item.Well.WellName,
                        WellType = new WellType()
                        {
                            ID = item.Well.WellType.ID,
                            Name = item.Well.WellType.Name
                        },
                        WellTypeID = item.Well.WellTypeID
                    }
                });
            }
            
            return Ok(ls);
        }

        // GET: api/WellLayers/5
        [ResponseType(typeof(WellLayer))]
        public IHttpActionResult GetWellLayer(long id)
        {
            WellLayer item = db.WellLayers.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            WellLayer wellLayer = new WellLayer()
            {
                ID = item.ID,
                EndPoint = item.EndPoint,
                RockTypeID = item.RockTypeID,
                StartPoint = item.StartPoint,
                WellID = item.WellID,
                RockType = new RockType()
                {
                    ID = item.RockTypeID,
                    BackgroundColor = item.RockType.BackgroundColor,
                    Name = item.RockType.Name
                },
                Well = new Well()
                {
                    ID = item.Well.ID,
                    Capacity = item.Well.Capacity,
                    GasOilDepth = item.Well.GasOilDepth,
                    WellName = item.Well.WellName,
                    WellType = new WellType()
                    {
                        ID = item.Well.WellType.ID,
                        Name = item.Well.WellType.Name
                    },
                    WellTypeID = item.Well.WellTypeID
                }
            };

            return Ok(wellLayer);
        }

        // PUT: api/WellLayers/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutWellLayer(long id, WellLayer wellLayer)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != wellLayer.ID)
            {
                return BadRequest();
            }

            db.Entry(wellLayer).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!WellLayerExists(id))
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

        // POST: api/WellLayers
        [ResponseType(typeof(WellLayer))]
        public IHttpActionResult PostWellLayer(WellLayer wellLayer)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.WellLayers.Add(wellLayer);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = wellLayer.ID }, wellLayer);
        }

        // DELETE: api/WellLayers/5
        [ResponseType(typeof(WellLayer))]
        public IHttpActionResult DeleteWellLayer(long id)
        {
            WellLayer wellLayer = db.WellLayers.Find(id);
            if (wellLayer == null)
            {
                return NotFound();
            }

            db.WellLayers.Remove(wellLayer);
            db.SaveChanges();

            return Ok(wellLayer);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool WellLayerExists(long id)
        {
            return db.WellLayers.Count(e => e.ID == id) > 0;
        }
    }
}