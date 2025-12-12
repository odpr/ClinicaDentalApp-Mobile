using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

[ApiController]
[Route("api/Pacientes")]
public class PacientesApiController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    public PacientesApiController(ApplicationDbContext context) => _context = context;

    [HttpGet]
    public async Task<IActionResult> Get() =>
        Ok(await _context.Pacientes.Select(p => new {
            id = p.Id,
            nombreCompleto = p.Nombre + " " + p.Apellido,
            documento = p.Documento
        }).ToListAsync());
}
