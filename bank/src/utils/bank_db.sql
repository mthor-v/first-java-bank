DO $$
DECLARE
    v_count integer;
    v_table_name_ varchar(20);
    v_sql text;
BEGIN
    v_table_name_ := 'users';
    SELECT count(1) INTO v_count FROM information_schema.tables WHERE table_name = v_table_name_;
    
    IF v_count = 0 THEN
        v_sql := format('
            CREATE TABLE IF NOT EXISTS %I (
                id SERIAL PRIMARY KEY,
                name VARCHAR(50),
                uid VARCHAR(20) UNIQUE NOT NULL,
                password VARCHAR(50) NOT NULL,
                balance SMALLINT NOT NULL
            )', v_table_name_);
        
        EXECUTE v_sql;
        
        EXECUTE format('COMMENT ON TABLE %I IS ''Tabla que contiene la información de usuarios del banco.''', v_table_name_);
        EXECUTE format('COMMENT ON COLUMN %I.name IS ''Nombre del usuario.''', v_table_name_);
        EXECUTE format('COMMENT ON COLUMN %I.uid IS ''Número único de identidad del usuario.''', v_table_name_);
        EXECUTE format('COMMENT ON COLUMN %I.password IS ''Contraseña alfanumerica de cuenta.''', v_table_name_);
        EXECUTE format('COMMENT ON COLUMN %I.balance IS ''Saldo de cuenta.''', v_table_name_);
        
        RAISE NOTICE 'Tabla % creada satisfactoriamente.', v_table_name_;
    ELSE 
        RAISE NOTICE 'Tabla % ya existe.', v_table_name_;
    END IF;
END $$;