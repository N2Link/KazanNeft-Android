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
    public class AssetsController : ApiController
    {
        private KazanNeftEntities db = new KazanNeftEntities();

        // GET: api/Assets
        public IHttpActionResult GetAssets()
        {
            List<Asset> list = new List<Asset>();
            foreach (var item in db.Assets)
            {
                list.Add(new Asset()
                {
                    AssetGroupID = item.AssetGroupID,
                    ID = item.ID,
                    AssetName = item.AssetName,
                    AssetSN = item.AssetSN,
                    DepartmentLocationID = item.DepartmentLocationID,
                    EmployeeID = item.EmployeeID,
                    Description = item.Description,
                    WarrantyDate = item.WarrantyDate,
                    AssetGroup = new AssetGroup()
                    {
                        ID = item.AssetGroup.ID,
                        Name = item.AssetGroup.Name
                    },
                    DepartmentLocation = new DepartmentLocation()
                    {
                        ID = item.DepartmentLocation.ID,
                        DepartmentID = item.DepartmentLocation.DepartmentID,
                        EndDate = item.DepartmentLocation.EndDate,
                        LocationID = item.DepartmentLocation.LocationID,
                        StartDate = item.DepartmentLocation.StartDate,
                        Department = new Department()
                        {
                            ID = item.DepartmentLocation.DepartmentID,
                            Name = item.DepartmentLocation.Department.Name,
                        },
                        Location = new Location()
                        {
                            ID = item.DepartmentLocation.LocationID,
                            Name = item.DepartmentLocation.Location.Name,
                        }
                    },
                    Employee = new Employee()
                    {
                        ID = item.Employee.ID,
                        FirstName = item.Employee.FirstName,
                        LastName = item.Employee.LastName, 
                        Phone = item.Employee.Phone,
                    }
                    
                });
            }
            return Ok(list);
        }

        // GET: api/Assets/5
        [ResponseType(typeof(Asset))]
        public IHttpActionResult GetAsset(long id)
        {
            Asset item = db.Assets.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            Asset asset = new Asset()
            {
                AssetGroupID = item.ID,
                ID = item.ID,
                AssetName = item.AssetName,
                AssetSN = item.AssetSN,
                DepartmentLocationID = item.DepartmentLocationID,
                EmployeeID = item.EmployeeID,
                Description = item.Description,
                WarrantyDate = item.WarrantyDate,
                AssetGroup = new AssetGroup()
                {
                    ID = item.AssetGroup.ID,
                    Name = item.AssetGroup.Name
                },
                DepartmentLocation = new DepartmentLocation()
                {
                    ID = item.DepartmentLocation.ID,
                    DepartmentID = item.DepartmentLocation.DepartmentID,
                    EndDate = item.DepartmentLocation.EndDate,
                    LocationID = item.DepartmentLocation.LocationID,
                    StartDate = item.DepartmentLocation.StartDate,
                    Department = new Department()
                    {
                        ID = item.DepartmentLocation.DepartmentID,
                        Name = item.DepartmentLocation.Department.Name,
                    },
                    Location = new Location()
                    {
                        ID = item.DepartmentLocation.LocationID,
                        Name = item.DepartmentLocation.Location.Name,
                    }
                },
                    Employee = new Employee()
                    {
                        ID = item.Employee.ID,
                        FirstName = item.Employee.FirstName,
                        LastName = item.Employee.LastName,
                        Phone = item.Employee.Phone,
                    }

            };

            return Ok(asset);
        }

        // PUT: api/Assets/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutAsset(long id, Asset asset)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != asset.ID)
            {
                return BadRequest();
            }

            db.Entry(asset).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AssetExists(id))
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

        // POST: api/Assets
        [ResponseType(typeof(Asset))]
        public IHttpActionResult PostAsset(Asset asset)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Assets.Add(asset);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = asset.ID }, asset);
        }

        // DELETE: api/Assets/5
        [ResponseType(typeof(Asset))]
        public IHttpActionResult DeleteAsset(long id)
        {
            Asset asset = db.Assets.Find(id);
            if (asset == null)
            {
                return NotFound();
            }

            db.Assets.Remove(asset);
            db.SaveChanges();

            return Ok(asset);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool AssetExists(long id)
        {
            return db.Assets.Count(e => e.ID == id) > 0;
        }
    }
}