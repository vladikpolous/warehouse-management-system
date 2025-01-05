CREATE PROCEDURE create_default_user(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(150),
    IN p_password VARCHAR(255),
    IN p_role_name VARCHAR(100)
)
BEGIN
    DECLARE v_role_id BIGINT;

    -- Find a role by name
    SELECT role_id
    INTO v_role_id
    FROM roles
    WHERE role_name = p_role_name;

    -- If the role is not found, create a new one
    IF v_role_id IS NULL THEN
        INSERT INTO roles (role_name, role_description)
        VALUES (p_role_name, 'Default main system role');
        SET v_role_id = LAST_INSERT_ID();
    END IF;

    -- Add a user if they don't already exist
    IF NOT EXISTS (SELECT 1
                   FROM users
                   WHERE email = p_email) THEN
        INSERT INTO users (name, email, password, role_id)
        VALUES (p_name, p_email, p_password, v_role_id);
    END IF;
END;

-- Calling a procedure to create a default ADMIN
CALL create_default_user(
        'Admin User',
        'admin@warehouse.com',
        'admin_password',
        'ADMIN'
     );