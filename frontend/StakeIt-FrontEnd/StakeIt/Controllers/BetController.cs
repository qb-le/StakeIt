using Microsoft.AspNetCore.Mvc;

namespace StakeIt.Controllers
{
    public class BetController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
