package integrado.prog2.services;

import integrado.prog2.entities.PerfilUsuario;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exceptions.DatoInvalidoException;
import integrado.prog2.exceptions.DuplicadoException;
import integrado.prog2.exceptions.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios;
    private long siguienteId;
    private long siguientePerfilId;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.siguienteId = 1;
        this.siguientePerfilId = 1;
    }

    public Usuario crear(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol, String direccion, String observaciones)
            throws DatoInvalidoException, DuplicadoException {
        validarUsuario(nombre, apellido, mail, celular, contrasena, rol, direccion, null);
        PerfilUsuario perfil = new PerfilUsuario(siguientePerfilId++, direccion.trim(), normalizarObservaciones(observaciones));
        Usuario usuario = new Usuario(siguienteId++, nombre.trim(), apellido.trim(), mail.trim(), celular.trim(), contrasena.trim(), rol, perfil);
        usuarios.add(usuario);
        return usuario;
    }

    public List<Usuario> listarActivos() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado()) {
                activos.add(usuario);
            }
        }
        return activos;
    }

    public Usuario buscarActivoPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id) && !usuario.isEliminado()) {
                return usuario;
            }
        }
        throw new EntidadNoEncontradaException("No se encontro un usuario activo con ID " + id + ".");
    }

    public void editar(Long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol, String direccion, String observaciones)
            throws EntidadNoEncontradaException, DatoInvalidoException, DuplicadoException {
        Usuario usuario = buscarActivoPorId(id);
        validarUsuario(nombre, apellido, mail, celular, contrasena, rol, direccion, id);
        usuario.setNombre(nombre.trim());
        usuario.setApellido(apellido.trim());
        usuario.setMail(mail.trim());
        usuario.setCelular(celular.trim());
        usuario.setContrasena(contrasena.trim());
        usuario.setRol(rol);
        usuario.getPerfil().setDireccion(direccion.trim());
        usuario.getPerfil().setObservaciones(normalizarObservaciones(observaciones));
    }

    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Usuario usuario = buscarActivoPorId(id);
        usuario.setEliminado(true);
    }

    public void validarNombre(String nombre) throws DatoInvalidoException {
        validarTexto(nombre, "El nombre es obligatorio.");
        if (!nombre.trim().matches("[\\p{L} ]+")) {
            throw new DatoInvalidoException("El nombre solo puede contener letras y espacios.");
        }
    }

    public void validarApellido(String apellido) throws DatoInvalidoException {
        validarTexto(apellido, "El apellido es obligatorio.");
        if (!apellido.trim().matches("[\\p{L} ]+")) {
            throw new DatoInvalidoException("El apellido solo puede contener letras y espacios.");
        }
    }

    public void validarMailDisponible(String mail, Long idIgnorado) throws DatoInvalidoException, DuplicadoException {
        validarTexto(mail, "El mail es obligatorio.");
        if (!mail.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new DatoInvalidoException("El formato del mail no es valido.");
        }
        if (existeMail(mail, idIgnorado)) {
            if (idIgnorado == null) {
                throw new DuplicadoException("Ya existe un usuario con ese mail.");
            }
            throw new DuplicadoException("Ya existe otro usuario con ese mail.");
        }
    }

    public void validarCelular(String celular) throws DatoInvalidoException {
        validarTexto(celular, "El celular es obligatorio.");
        if (!celular.trim().matches("\\d{8,15}")) {
            throw new DatoInvalidoException("El celular debe contener solo numeros y tener entre 8 y 15 digitos.");
        }
    }

    public void validarContrasena(String contrasena) throws DatoInvalidoException {
        validarTexto(contrasena, "La contrasena es obligatoria.");
        if (contrasena.trim().length() < 4) {
            throw new DatoInvalidoException("La contrasena debe tener al menos 4 caracteres.");
        }
    }

    public void validarDireccion(String direccion) throws DatoInvalidoException {
        validarTexto(direccion, "La direccion del perfil es obligatoria.");
    }

    private boolean existeMail(String mail, Long idIgnorado) {
        for (Usuario usuario : usuarios) {
            boolean mismoMail = usuario.getMail().equalsIgnoreCase(mail.trim());
            boolean mismoId = idIgnorado != null && usuario.getId().equals(idIgnorado);
            if (!usuario.isEliminado() && mismoMail && !mismoId) {
                return true;
            }
        }
        return false;
    }

    private void validarUsuario(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol, String direccion, Long idIgnorado)
            throws DatoInvalidoException, DuplicadoException {
        validarNombre(nombre);
        validarApellido(apellido);
        validarMailDisponible(mail, idIgnorado);
        validarCelular(celular);
        validarContrasena(contrasena);
        if (rol == null) throw new DatoInvalidoException("El rol es obligatorio.");
        validarDireccion(direccion);
    }

    private void validarTexto(String texto, String mensaje) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    private String normalizarObservaciones(String observaciones) {
        return observaciones == null ? "" : observaciones.trim();
    }
}
