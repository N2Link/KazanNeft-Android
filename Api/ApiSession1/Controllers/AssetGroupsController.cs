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
    public class AssetGroupsController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/AssetGroups
        public IHttpActionResult  GetAssetGroups()
        {
            List<AssetGroup> list = new List<AssetGroup>();
            foreach (var item in db.AssetGroups)
            {
                list.Add(new AssetGroup()
                {
                    ID = item.ID,
                    Name = item.Name
                });
            }
            return Ok(list);
        }

        // GET: api/AssetGroups/5
        [ResponseType(typeof(AssetGroup))]
        public IHttpActionResult GetAssetGroup(long id)
        {
            AssetGroup item = db.AssetGroups.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            AssetGroup assetGroup = new AssetGroup()
            {
                ID = item.ID,
                Name = item.Name
            };

            return Ok(assetGroup);
        }

        // PUT: api/AssetGroups/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutAssetGroup(long id, AssetGroup assetGroup)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != assetGroup.ID)
            {
                return BadRequest();
            }

            db.Entry(assetGroup).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AssetGroupExists(id))
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

        // POST: api/AssetGroups
        [ResponseType(typeof(AssetGroup))]
        public IHttpActionResult PostAssetGroup(AssetGroup assetGroup)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.AssetGroups.Add(assetGroup);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = assetGroup.ID }, assetGroup);
        }

        // DELETE: api/AssetGroups/5
        [ResponseType(typeof(AssetGroup))]
        public IHttpActionResult DeleteAssetGroup(long id)
        {
            AssetGroup assetGroup = db.AssetGroups.Find(id);
            if (assetGroup == null)
            {
                return NotFound();
            }

            db.AssetGroups.Remove(assetGroup);
            db.SaveChanges();

            return Ok(assetGroup);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool AssetGroupExists(long id)
        {
            return db.AssetGroups.Count(e => e.ID == id) > 0;
        }
    }
}