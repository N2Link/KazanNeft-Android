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
    public class AssetTransferLogsController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/AssetTransferLogs
        public IHttpActionResult GetAssetTransferLogs()
        {
            List<AssetTransferLog> list = new List<AssetTransferLog>();
            foreach (var item in db.AssetTransferLogs)
            {
                list.Add(new AssetTransferLog()
                {
                    AssetID = item.AssetID,
                    FromAssetSN = item.FromAssetSN,
                    ToAssetSN = item.ToAssetSN,
                    ID = item.ID,
                    TransferDate = item.TransferDate,
                    FromDepartmentLocationID = item.FromDepartmentLocationID,
                    ToDepartmentLocationID = item.ToDepartmentLocationID,
                    
                    
                    DepartmentLocation = new DepartmentLocation()
                    {
                        Department = new Department()
                        {
                            Name = item.DepartmentLocation.Department.Name
                        }
                    },
                    DepartmentLocation1 = new DepartmentLocation()
                    {
                        Department = new Department()
                        {
                            Name = item.DepartmentLocation1.Department.Name
                        }
                    }
                });
            }
            return Ok(list);
        }

        // GET: api/AssetTransferLogs/5
        [ResponseType(typeof(AssetTransferLog))]
        public IHttpActionResult GetAssetTransferLog(long id)
        {
            AssetTransferLog item = db.AssetTransferLogs.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            AssetTransferLog assetTransferLog= new AssetTransferLog()
            {
                AssetID = item.AssetID,
                FromAssetSN = item.FromAssetSN,
                ToAssetSN = item.ToAssetSN,
                ID = item.ID,
                TransferDate = item.TransferDate,
                FromDepartmentLocationID = item.FromDepartmentLocationID,
                ToDepartmentLocationID = item.ToDepartmentLocationID,
                DepartmentLocation = new DepartmentLocation()
                {
                    Department = new Department()
                    {
                        Name = item.DepartmentLocation.Department.Name
                    }
                },         
                DepartmentLocation1 = new DepartmentLocation()
                {
                    Department = new Department()
                    {
                        Name = item.DepartmentLocation1.Department.Name
                    }
                }
                
            };

            return Ok(assetTransferLog);
        }

        // PUT: api/AssetTransferLogs/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutAssetTransferLog(long id, AssetTransferLog assetTransferLog)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != assetTransferLog.ID)
            {
                return BadRequest();
            }

            db.Entry(assetTransferLog).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AssetTransferLogExists(id))
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

        // POST: api/AssetTransferLogs
        [ResponseType(typeof(AssetTransferLog))]
        public IHttpActionResult PostAssetTransferLog(AssetTransferLog assetTransferLog)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.AssetTransferLogs.Add(assetTransferLog);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = assetTransferLog.ID }, assetTransferLog);
        }

        // DELETE: api/AssetTransferLogs/5
        [ResponseType(typeof(AssetTransferLog))]
        public IHttpActionResult DeleteAssetTransferLog(long id)
        {
            AssetTransferLog assetTransferLog = db.AssetTransferLogs.Find(id);
            if (assetTransferLog == null)
            {
                return NotFound();
            }

            db.AssetTransferLogs.Remove(assetTransferLog);
            db.SaveChanges();

            return Ok(assetTransferLog);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool AssetTransferLogExists(long id)
        {
            return db.AssetTransferLogs.Count(e => e.ID == id) > 0;
        }
    }
}