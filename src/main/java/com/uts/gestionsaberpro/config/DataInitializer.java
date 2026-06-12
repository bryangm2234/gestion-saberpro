package com.uts.gestionsaberpro.config;

import com.uts.gestionsaberpro.entity.*;
import com.uts.gestionsaberpro.entity.Role.RoleName;
import com.uts.gestionsaberpro.repository.*;
import com.uts.gestionsaberpro.service.BeneficioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final BeneficioService beneficioService;

    public DataInitializer(PasswordEncoder pe, BeneficioService bs) {
        this.passwordEncoder = pe;
        this.beneficioService = bs;
    }

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepo, UsuarioRepository usuarioRepo,
            FacultadRepository facultadRepo, ProgramaRepository programaRepo,
            CoordinadorRepository coordRepo, DocenteRepository docenteRepo,
            EstudianteRepository estudianteRepo, ResultadoRepository resultadoRepo) {
        return args -> {
            if (roleRepo.count() > 0) {
                System.out.println("✅ DataInitializer: datos ya existen, omitiendo.");
                return;
            }
            System.out.println("🚀 DataInitializer: creando datos...");

            // ROLES
            Role rAdmin = roleRepo.save(new Role(null, RoleName.ROLE_ADMIN));
            Role rCoord = roleRepo.save(new Role(null, RoleName.ROLE_COORDINADOR));
            Role rDoc   = roleRepo.save(new Role(null, RoleName.ROLE_DOCENTE));
            Role rEst   = roleRepo.save(new Role(null, RoleName.ROLE_ESTUDIANTE));

            // FACULTADES
            Facultad fIngenieria = facultadRepo.save(new Facultad("Facultad de Ingenierías y Tecnologías", "FING"));
            facultadRepo.save(new Facultad("Facultad de Ciencias Básicas", "FCIB"));

            // PROGRAMAS
            Programa pSistemas = new Programa("Ingeniería de Sistemas", Programa.TipoPrograma.PROFESIONAL);
            pSistemas.setFacultad(fIngenieria);
            pSistemas = programaRepo.save(pSistemas);

            Programa pTecDesarrollo = new Programa("Tecnología en Desarrollo de Software", Programa.TipoPrograma.TECNOLOGIA);
            pTecDesarrollo.setFacultad(fIngenieria);
            pTecDesarrollo = programaRepo.save(pTecDesarrollo);

            Programa pTecInformatica = new Programa("Tecnología en Informática", Programa.TipoPrograma.TECNOLOGIA);
            pTecInformatica.setFacultad(fIngenieria);
            pTecInformatica = programaRepo.save(pTecInformatica);

            // ADMIN
            Usuario uAdmin = new Usuario();
            uAdmin.setDocumento("1000000001");
            uAdmin.setTipoDocumento(Usuario.TipoDocumento.CC);
            uAdmin.setPrimerNombre("Carlos");
            uAdmin.setPrimerApellido("García");
            uAdmin.setCorreo("admin@saberpro.edu.co");
            uAdmin.setPassword(passwordEncoder.encode("polar28"));
            uAdmin.setRoles(Set.of(rAdmin));
            usuarioRepo.save(uAdmin);

            // COORDINADOR — guardar usuario primero, luego crear Coordinador SIN cascade
            Usuario uCoord = new Usuario();
            uCoord.setDocumento("1000000002");
            uCoord.setTipoDocumento(Usuario.TipoDocumento.CC);
            uCoord.setPrimerNombre("María");
            uCoord.setPrimerApellido("López");
            uCoord.setCorreo("coordinador@saberpro.edu.co");
            uCoord.setPassword(passwordEncoder.encode("polar28"));
            uCoord.setRoles(Set.of(rCoord));
            uCoord = usuarioRepo.save(uCoord);

            Coordinador coord = new Coordinador();
            coord.setUsuario(uCoord);
            coord.setAreaAsignada("Ingeniería de Sistemas");
            coordRepo.save(coord);

            // DOCENTE — mismo patrón
            Usuario uDoc = new Usuario();
            uDoc.setDocumento("1000000003");
            uDoc.setTipoDocumento(Usuario.TipoDocumento.CC);
            uDoc.setPrimerNombre("Pedro");
            uDoc.setPrimerApellido("Martínez");
            uDoc.setCorreo("docente@saberpro.edu.co");
            uDoc.setPassword(passwordEncoder.encode("polar28"));
            uDoc.setRoles(Set.of(rDoc));
            uDoc = usuarioRepo.save(uDoc);

            Docente docente = new Docente();
            docente.setUsuario(uDoc);
            docente.setFacultad(fIngenieria);
            docente.setEspecialidad("Ingeniería de Sistemas");
            docenteRepo.save(docente);

            // ESTUDIANTES CON DATOS REALES DEL EXCEL
            LocalDate fechaExamen = LocalDate.of(2026, 3, 1);
            Object[][] datos = {
                {"BARBOSA","Juan Sebastián","1030678901","EK20183007722",128,182,202,206,183,185,160,197,"B1",pSistemas},
                {"QUINTERO","María Alejandra","1030658785","EK20183140703",125,151,179,163,205,182,144,136,"B2",pSistemas},
                {"PARRA","Carlos Andrés","1098765434","EK20183040545",159,172,182,142,165,167,132,148,"A2",pSistemas},
                {"ANAYA","Laura Camila","1098765435","EK20183025381",146,199,157,149,147,174,127,171,"A2",pSistemas},
                {"FLOR","Daniel Felipe","1098765436","EK20183025335",198,153,147,157,146,168,114,160,"A2",pSistemas},
                {"GARCIA","Sofía Valentina","1098765437","EK20183122648",179,172,158,140,136,128,121,142,"A1",pSistemas},
                {"MANOSALVA","José David","1098765438","EK20183064605",115,152,159,172,165,142,118,119,"A2",pSistemas},
                {"MENDOZA","Ana María","1098765439","EK20183187351",132,123,125,169,204,173,127,171,"B2",pSistemas},
                {"BELTRAN","Santiago","1032183820","EK20183233820",86,187,160,171,148,162,125,142,"A2",pSistemas},
                {"SANTAMARIA","Valentina","1032183016","EK20183030016",175,149,145,158,125,162,76,125,"A1",pSistemas},
                {"SANCHEZ","Luisa Fernanda","1032147073","EK20183047073",209,143,117,129,147,137,125,136,"A2",pTecDesarrollo},
                {"ROMERO","Andrés Felipe","1032136451","EK20183236451",93,183,155,164,133,174,130,154,"A1",pTecDesarrollo},
                {"LUNA","Camila Andrea","1032141714","EK20183041714",125,157,138,135,152,176,128,165,"A2",pTecDesarrollo},
                {"TRIANA","Juan Pablo","1032187801","EK20183187801",150,136,145,150,126,148,129,131,"A1",pTecDesarrollo},
                {"SUAREZ","Daniela","1032176566","EK20183176566",128,146,146,132,147,130,110,125,"A2",pTecDesarrollo},
                {"GARCIA","Miguel Angel","1032104427","EK20183204427",129,138,148,146,135,109,107,131,"A1",pTecDesarrollo},
                {"PINZON","Karla Sofía","1032196280","EK20183196280",153,123,127,147,140,145,143,160,"A1",pTecInformatica},
                {"JAIMES","Nicolás Andrés","1032173799","EK20183173799",166,157,124,100,140,100,105,113,"A1",pTecInformatica},
                {"NIÑO","Yuliana Paola","1032109565","EK20183009565",165,137,136,118,116,146,122,154,"A0",pTecInformatica},
                {"FABIAN","Sebastián","1032117756","EK20183117756",139,93,168,150,114,102,123,94,"A0",pTecInformatica},
                {"HERNANDEZ","Paula Andrea","1032144579","EK20183044579",116,166,136,104,140,158,125,154,"A1",pTecInformatica},
                {"LARIOS","Jorge Luis","1032145760","EK20183045760",149,123,129,121,131,101,102,165,"A1",pSistemas},
                {"CALDERON","Diana Carolina","1032134044","EK20183034044",127,147,134,111,131,65,112,94,"A1",pSistemas},
                {"VILLARREAL","Kevin Stiven","1032141521","EK20183041521",96,162,114,131,144,122,112,131,"A1",pSistemas},
                {"RESTREPO","Manuela","1032127436","EK20183027436",81,134,126,149,139,127,136,142,"A1",pTecDesarrollo},
                {"CACERES","Brayan Alexis","1032131592","EK20183031592",124,135,108,92,165,132,104,131,"A2",pTecDesarrollo},
                {"TABARES","Natalia","1032104153","EK20183004153",131,131,107,88,162,136,112,148,"A2",pTecDesarrollo},
                {"NARANJO","Cristian Camilo","1032130783","EK20183030783",166,113,113,112,106,135,117,119,"A0",pTecInformatica},
                {"PRADA","Leidy Johana","1032124754","EK20183024754",119,125,137,107,123,83,104,119,"A1",pTecInformatica},
                {"VARGAS","Esteban","1032186200","EK20183186200",95,120,151,86,119,149,103,119,"A0",pTecInformatica},
                {"TORRES","Yenny Paola","1032182410","EK20183182410",109,105,104,103,142,102,135,80,"A1",pTecInformatica},
                {"ORTIZ","Luis Fernando","1032213735","EK20183213735",128,81,107,102,119,130,111,125,"A0",pSistemas},
                {"VILLAMIZAR","Andrea Marcela","1032165220","EK20183065220",134,96,92,110,97,83,107,119,"A0",pSistemas},
                {"RESTREPO","Oscar Mauricio","1032128123","EK20183028123",0,117,122,105,137,157,96,131,"A1",pSistemas},
            };

            int docCounter = 1;
            for (Object[] d : datos) {
                String apellido  = (String) d[0];
                String nombre    = (String) d[1];
                String doc       = (String) d[2];
                String registro  = (String) d[3];
                int comEsc       = (int) d[4];
                int razCuan      = (int) d[5];
                int lectCrit     = (int) d[6];
                int compCiu      = (int) d[7];
                int ingles       = (int) d[8];
                int formProy     = (int) d[9];
                int pensCient    = (int) d[10];
                int disSw        = (int) d[11];
                String nivIng    = (String) d[12];
                Programa prog    = (Programa) d[13];

                String correo = "estudiante" + docCounter + "@saberpro.edu.co";
                docCounter++;

                // Crear y guardar usuario
                Usuario u = new Usuario();
                u.setDocumento(doc);
                u.setTipoDocumento(Usuario.TipoDocumento.CC);
                String[] partes = nombre.split(" ");
                u.setPrimerNombre(partes[0]);
                if (partes.length > 1) u.setSegundoNombre(partes[1]);
                u.setPrimerApellido(apellido);
                u.setCorreo(correo);
                u.setPassword(passwordEncoder.encode(doc));
                u.setRoles(Set.of(rEst));
                u = usuarioRepo.save(u);

                // Crear estudiante SIN cascade en usuario
                Estudiante est = new Estudiante();
                est.setUsuario(u);
                est.setPrograma(prog);
                est.setNumeroRegistro(registro);
                est.setSemestre(10);
                est = estudianteRepo.save(est);

                // Crear resultado
                ResultadoSaberPro r = new ResultadoSaberPro();
                r.setEstudiante(est);
                r.setFechaExamen(fechaExamen);
                r.setComunicacionEscrita(bd(comEsc));
                r.setRazonamientoCuantitativo(bd(razCuan));
                r.setLecturaCritica(bd(lectCrit));
                r.setCompetenciasCiudadanas(bd(compCiu));
                r.setIngles(bd(ingles));
                r.setFormulacionProyectos(bd(formProy));
                r.setPensamientoCientifico(bd(pensCient));
                r.setDisenioSoftware(bd(disSw));
                r.setNivelIngles(nivIng);
                r.calcularPuntajeYNivel();
                beneficioService.calcularBeneficios(r);
                resultadoRepo.save(r);
            }

            System.out.println("✅ DataInitializer: " + datos.length + " estudiantes creados.");
            System.out.println("🔑 Admin      : admin@saberpro.edu.co       / polar28");
            System.out.println("🔑 Coordinador: coordinador@saberpro.edu.co / polar28");
            System.out.println("🔑 Docente    : docente@saberpro.edu.co     / polar28");
            System.out.println("🔑 Estudiante1: estudiante1@saberpro.edu.co / 1030678901");
        };
    }

    private BigDecimal bd(int v) {
        return new BigDecimal(v);
    }
}
