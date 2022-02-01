package web;

import datos.ClienteDaoJdbc;
import dominio.Cliente;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch(accion){
                case "editar":
                    this.editarCliente(request, response);
                    break;
                case "eliminar":
                    this.eliminarCliente(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
    }
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<Cliente> clientes = new ClienteDaoJdbc().listar();
        System.out.println("clientes = " + clientes);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", this.calcularSaldo(clientes));
        response.sendRedirect("clientes.jsp");
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);
    }
    
    private double calcularSaldo(List<Cliente> clientes){
        double saldoTotal = 0;
        for(Cliente cliente:clientes){
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }
    
    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente cliente = new ClienteDaoJdbc().encontrar(new Cliente(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        //Recuperamos los valores del formulario AgregarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        //Creamos el objeto Cliente
        Cliente cliente = new Cliente(idCliente);
        //Eliminamos en la BBDD
        int registrosModificados = new ClienteDaoJdbc().delete(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        //redirigimos hacia la accion default;
        this.accionDefault(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarCliente(request, response);
                    break;
                case "modificar":
                    this.modificarCliente(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
    }
    
    private void modificarCliente(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        //Recuperamos los valores del formulario AgregarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        
        //Creamos el objeto Cliente
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        //Actualizamos en la BBDD
        int registrosModificados = new ClienteDaoJdbc().update(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        //redirigimos hacia la accion default;
        this.accionDefault(request, response);
    }
    
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        //Recuperamos los valores del formulario AgregarCliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        
        //Creamos el objeto Cliente
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        //Insertamos en la BBDD
        int registrosModificados = new ClienteDaoJdbc().insert(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        //redirigimos hacia la accion default;
        this.accionDefault(request, response);
    }
}
