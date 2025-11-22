using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

[ApiController]
[Route("api/[controller]")]
public class PacientesController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    public PacientesController(ApplicationDbContext context) => _context = context;

    [HttpGet]
    public async Task<IActionResult> Get() =>
        Ok(await _context.Pacientes.Select(p => new {
            id = p.Id,
            nombreCompleto = p.Nombre + " " + p.Apellido,
            documento = p.Documento
        }).ToListAsync());
}
