# Proyecto: Gestión de Clientes, Cuentas y Préstamos

Este proyecto implementa un sistema para gestionar clientes, cuentas bancarias y préstamos en una aplicación empresarial. Proporciona funcionalidades completas para crear, buscar y manejar estas entidades, incluyendo validaciones y operaciones específicas.

## Descripción del Proyecto

El sistema permite:
- **Gestionar clientes**: Crear nuevos clientes y buscar por DNI.
- **Gestionar cuentas bancarias**: Crear cuentas asociadas a clientes y realizar operaciones sobre ellas.
- **Gestionar préstamos**: Solicitar préstamos y realizar pagos de cuotas.

El proyecto está construido en **Java**, utilizando el framework **Spring** para crear servicios RESTful robustos.

## Estructura del Proyecto

### Controladores

1. **ClienteController.java**: Define los endpoints para gestionar clientes:
   - `POST /api/cliente`: Crea un nuevo cliente.
   - `GET /api/cliente/{dni}`: Busca un cliente por su DNI.

2. **CuentaController.java**: Maneja la gestión de cuentas bancarias:
   - `POST /api/cuenta`: Crea una nueva cuenta asociada a un cliente.
   - `GET /api/cuenta/{numeroCuenta}`: Busca una cuenta por su número.

3. **PrestamoController.java**: Gestiona las operaciones relacionadas con préstamos:
   - `POST /api/prestamo`: Solicita un nuevo préstamo.
   - `GET /api/prestamo/{dni}`: Lista los préstamos de un cliente.
   - `POST /api/prestamo/{id}`: Realiza el pago de una cuota de un préstamo.

### Servicios

1. **ClienteService.java**: Contiene la lógica para gestionar clientes, incluyendo validaciones como:
   - Crear clientes mayores de edad y únicos por DNI.
   - Asignar cuentas y préstamos a clientes existentes.

2. **CuentaService.java**: Gestiona las cuentas bancarias:
   - Crear nuevas cuentas asociadas a un cliente con validaciones de tipo y moneda.
   - Actualizar saldos de cuentas al gestionar préstamos.
   - Validar suficiencia de fondos para operaciones.

3. **PrestamoService.java**: Maneja la lógica de negocio para préstamos:
   - Solicitar préstamos con validaciones de montos y plazos.
   - Calcular el plan de pago de un préstamo.
   - Registrar el pago de cuotas.

4. **ScoreCreditService.java**: Simula un sistema de calificación crediticia para evaluar si un cliente es apto para un préstamo.

### Persistencia

El proyecto utiliza DAOs para gestionar la interacción con la base de datos, como `ClienteDao`, `CuentaDao` y `PrestamoDao`.

### Excepciones

El sistema utiliza excepciones personalizadas para manejar errores de manera detallada, como:
- `ClienteAlreadyExistsException`, `ClienteMenorDeEdadException`, `CuentaNotFoundException`.
- `PrestamoNotFoundException`, `NoAlcanzaException`, entre otras.

## Requisitos Previos

- **Java 17** o superior.
- **Maven** para la gestión de dependencias.
- **Spring Boot** (versión 2.7 o superior).
- Base de datos configurada según las dependencias de los DAOs.

## Ejemplo de Uso

### Crear Cliente
```json
POST /api/cliente
{
  "nombre": "Juan Perez",
  "dni": 12345678,
  "edad": 30,
  "tipoPersona": "FISICA"
}
```

### Crear Cuenta
```json
POST /api/cuenta
{
  "numeroCuenta": 1001,
  "dniTitular": 12345678,
  "tipoCuenta": "CAJA_AHORRO",
  "moneda": "PESOS",
  "balance": 0.0
}
```

### Solicitar Préstamo
```json
POST /api/prestamo
{
  "numeroCliente": 12345678,
  "monto": 100000,
  "plazoMeses": 12,
  "moneda": "PESOS"
}
```
