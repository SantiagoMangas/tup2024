package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;
        Cliente cliente =   ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
        if (loadComplete) {
            for (Cuenta cuenta :
                    cuentaDao.getCuentasByCliente(dni)) {
                cliente.addCuenta(cuenta);
            }
        }
        return cliente;

    }
    public List<Cliente> findAll() {
            try {
                List<Cliente> clientes = new ArrayList<>();
                for (Object object : getInMemoryDatabase().values()) {
                    clientes.add(((ClienteEntity) object).toCliente());
                }
                return clientes;
            } catch (Exception e) {
                throw new RuntimeException("Error al buscar todos los clientes: " + e.getMessage(), e);
            }
        }
    
    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
