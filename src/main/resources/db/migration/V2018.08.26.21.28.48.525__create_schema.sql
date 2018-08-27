CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.hibernate_sequence OWNER TO tasks;

CREATE TABLE public.tasks (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    date_time timestamp without time zone NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    created_by_id bigint,
    last_modified_by_id bigint,
    user_id bigint NOT NULL
);

ALTER TABLE public.tasks OWNER TO tasks;

CREATE TABLE public.users (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    first_name character varying(255),
    last_name character varying(255),
    username character varying(255) NOT NULL,
    created_by_id bigint,
    last_modified_by_id bigint
);

ALTER TABLE public.users OWNER TO tasks;

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT username_uidx UNIQUE (username);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE INDEX user_id_idx ON public.tasks USING btree (user_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT last_modified_by_id_fk FOREIGN KEY (last_modified_by_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT created_by_id_fk FOREIGN KEY (created_by_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT last_modified_by_id_fk FOREIGN KEY (last_modified_by_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT created_by_id_fk FOREIGN KEY (created_by_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id);