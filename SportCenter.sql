PGDMP  "                     |            SportCenters    16.0    16.0 {    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    17281    SportCenters    DATABASE     �   CREATE DATABASE "SportCenters" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Romanian_Romania.1250';
    DROP DATABASE "SportCenters";
                postgres    false            r           1247    17290    days    TYPE     �   CREATE TYPE public.days AS ENUM (
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday',
    'Sunday'
);
    DROP TYPE public.days;
       public          postgres    false            �           1247    20069    gender_enum1    TYPE     L   CREATE TYPE public.gender_enum1 AS ENUM (
    'M',
    'F',
    'Others'
);
    DROP TYPE public.gender_enum1;
       public          postgres    false            �            1255    19871    check_players()    FUNCTION     	  CREATE FUNCTION public.check_players() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin 
	if ( 
		select max_players 
		from sport s
		join sport_field sf on s.sport_id = sf.sport_id
		join field f on f.field_id = sf.field_id
		where f.field_id = new.field_id	
		)<=(
		select count(*)
		from members m
		join _groups g on m.group_id = g.group_id
		where g.group_id = new.reserved_by
		group by m.group_id
		)
		then 
			raise exception 'The team is already full!';
		end if;
	return new;
end
$$;
 &   DROP FUNCTION public.check_players();
       public          postgres    false            �            1255    19990 ^   checkaddressexists(character varying, character varying, character varying, character varying)    FUNCTION       CREATE FUNCTION public.checkaddressexists(in_country character varying, in_county character varying, in_city character varying, in_address_details character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    address_exists BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM address
        WHERE country = in_country
        AND county = in_county
        AND city = in_city
        AND address_details = in_address_details
    ) INTO address_exists;

    RETURN address_exists;
END;
$$;
 �   DROP FUNCTION public.checkaddressexists(in_country character varying, in_county character varying, in_city character varying, in_address_details character varying);
       public          postgres    false                       1255    20092 P   checkreservationoverlap(integer, time without time zone, time without time zone)    FUNCTION     �  CREATE FUNCTION public.checkreservationoverlap(new_field_id integer, new_start_time time without time zone, new_end_time time without time zone) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    overlapping_reservations INT;
BEGIN
    -- Check if there are any overlapping reservations for the same field
    SELECT COUNT(*)
    INTO overlapping_reservations
    FROM reservation
    WHERE field_id = new_field_id
        AND (
            (start_time <= new_start_time AND end_time > new_start_time)
            OR (start_time < new_end_time AND end_time >= new_end_time)
        );

    RETURN overlapping_reservations = 0; -- Return true if there are no overlapping reservations
END;
$$;
 �   DROP FUNCTION public.checkreservationoverlap(new_field_id integer, new_start_time time without time zone, new_end_time time without time zone);
       public          postgres    false            
           1255    20091 V   checkreservationoverlap(integer, date, time without time zone, time without time zone)    FUNCTION       CREATE FUNCTION public.checkreservationoverlap(new_field_id integer, new_reservation_date date, new_start_time time without time zone, new_end_time time without time zone) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    overlapping_reservations INT;
BEGIN
    -- Check if there are any overlapping reservations for the same field
    SELECT COUNT(*)
    INTO overlapping_reservations
    FROM reservation
    WHERE field_id = new_field_id
        AND reservation_date = new_reservation_date
        AND (
            (start_time <= new_start_time AND end_time > new_start_time)
            OR (start_time < new_end_time AND end_time >= new_end_time)
        );

    RETURN overlapping_reservations = 0; -- Return true if there are no overlapping reservations
END;
$$;
 �   DROP FUNCTION public.checkreservationoverlap(new_field_id integer, new_reservation_date date, new_start_time time without time zone, new_end_time time without time zone);
       public          postgres    false            �            1255    19991 ,   checksportexists(character varying, integer)    FUNCTION     x  CREATE FUNCTION public.checksportexists(in_sport_name character varying, in_max_players integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    sport_exists BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM sport
        WHERE sport_name = in_sport_name and max_players = in_max_players
    ) INTO sport_exists;

    RETURN sport_exists;
END;
$$;
 `   DROP FUNCTION public.checksportexists(in_sport_name character varying, in_max_players integer);
       public          postgres    false                       1255    20090 %   deletefieldandassociateddata(integer)    FUNCTION       CREATE FUNCTION public.deletefieldandassociateddata(field_id_to_delete integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    group_id_to_delete INT;
    member_id_to_delete INT;
BEGIN
    -- Find the group associated with the field
    SELECT reserved_by INTO group_id_to_delete
    FROM reservation
    WHERE field_id = field_id_to_delete
    LIMIT 1;

    -- Delete reservations for the field
    DELETE FROM reservation WHERE field_id = field_id_to_delete;
	DELETE FROM outside_field WHERE field_id = field_id_to_delete;
    DELETE FROM inside_field WHERE field_id = field_id_to_delete;
   DELETE FROM sport_field WHERE field_id = field_id_to_delete;
    -- Delete the group and its members
    IF group_id_to_delete IS NOT NULL THEN
        -- Find and delete members of the group
        FOR member_id_to_delete IN (SELECT member_id FROM members WHERE group_id = group_id_to_delete)
        LOOP
            DELETE FROM members WHERE member_id = member_id_to_delete;
        END LOOP;

        -- Delete the group
        DELETE FROM _groups WHERE group_id = group_id_to_delete;
    END IF;

    -- Finally, delete the field
    DELETE FROM field WHERE field_id = field_id_to_delete;

    RETURN true; -- Return true if the deletion is successful
END;
$$;
 O   DROP FUNCTION public.deletefieldandassociateddata(field_id_to_delete integer);
       public          postgres    false                       1255    20088 "   deletememberandcheckgroup(integer)    FUNCTION     U  CREATE FUNCTION public.deletememberandcheckgroup(member_id_to_delete integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    group_id_to_delete INT;
    is_admin_status BOOLEAN;
    new_admin_member_id INT;
BEGIN
    -- Get the group_id and admin status of the member to be deleted
    SELECT group_id, status_admin INTO group_id_to_delete, is_admin_status
    FROM members
    WHERE member_id = member_id_to_delete;

    -- Delete the member
    DELETE FROM members WHERE member_id = member_id_to_delete;

    -- Check if the member being deleted is an admin
    IF is_admin_status THEN
        -- Find another member from the same group to become admin
        SELECT member_id INTO new_admin_member_id
        FROM members
        WHERE group_id = group_id_to_delete
        LIMIT 1;

        -- If no other member found, delete the group and reservation
        IF new_admin_member_id IS NULL THEN
            DELETE FROM _groups WHERE group_id = group_id_to_delete;

            -- Delete reservations associated with the group
            DELETE FROM reservation WHERE reserved_by = group_id_to_delete;

            RETURN true; -- Deleted member and group, and no new admin found
        ELSE
            -- Update the new admin status to true
            UPDATE members
            SET status_admin = true
            WHERE member_id = new_admin_member_id;

            RETURN true; -- Deleted member, set new admin, and group still exists
        END IF;
    ELSE
        RETURN true; -- Deleted a regular member, no need to check group
    END IF;
END;
$$;
 M   DROP FUNCTION public.deletememberandcheckgroup(member_id_to_delete integer);
       public          postgres    false            	           1255    20089 +   deletememberandcheckgroup(integer, integer)    FUNCTION     �  CREATE FUNCTION public.deletememberandcheckgroup(member_id_to_delete integer, group_id_to_delete integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    is_admin_status BOOLEAN;
    new_admin_member_id INT;
BEGIN
    -- Get the admin status of the member to be deleted
    SELECT status_admin INTO is_admin_status
    FROM members
    WHERE member_id = member_id_to_delete AND group_id = group_id_to_delete;

    -- Delete the member
    DELETE FROM members WHERE member_id = member_id_to_delete AND group_id = group_id_to_delete;

    -- Check if the member being deleted is an admin
    IF is_admin_status THEN
        -- Find another member from the same group to become admin
        SELECT member_id INTO new_admin_member_id
        FROM members
        WHERE group_id = group_id_to_delete
        LIMIT 1;

        -- If no other member found, delete the group and reservation
        IF new_admin_member_id IS NULL then
            DELETE FROM reservation WHERE reserved_by = group_id_to_delete;

            DELETE FROM _groups WHERE group_id = group_id_to_delete;

            -- Delete reservations associated with the group

            RETURN true; -- Deleted member and group, and no new admin found
        ELSE
            -- Update the new admin status to true
            UPDATE members
            SET status_admin = true
            WHERE member_id = new_admin_member_id AND group_id = group_id_to_delete;

            RETURN true; -- Deleted member, set new admin, and group still exists
        END IF;
    ELSE
        RETURN true; -- Deleted a regular member, no need to check group
    END IF;
END;
$$;
 i   DROP FUNCTION public.deletememberandcheckgroup(member_id_to_delete integer, group_id_to_delete integer);
       public          postgres    false                       1255    20081    deleteperson(integer)    FUNCTION     G  CREATE FUNCTION public.deleteperson(person_id_to_delete integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    deletion_successful BOOLEAN := false;
BEGIN
    -- Use a BEGIN...EXCEPTION block to catch any exceptions that might occur during the deletion process
    BEGIN
        DELETE FROM feedback WHERE player_id = person_id_to_delete;
        DELETE FROM reservation WHERE reserved_by = person_id_to_delete;
        DELETE FROM members WHERE member_id = person_id_to_delete;
        DELETE FROM person WHERE person_id = person_id_to_delete;
        DELETE FROM _user WHERE user_id = person_id_to_delete;
        deletion_successful := true;

    EXCEPTION
        WHEN OTHERS THEN
            deletion_successful := false;
    END;

    -- Return the result
    RETURN deletion_successful;

END;
$$;
 @   DROP FUNCTION public.deleteperson(person_id_to_delete integer);
       public          postgres    false            �            1255    19872    update_payment()    FUNCTION     T  CREATE FUNCTION public.update_payment() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN 
    UPDATE payments 
    SET payment_amount = (
        SELECT price_hour * 
               EXTRACT(EPOCH FROM (reservation.end_time - reservation.start_time)) / 3600
        FROM field
        JOIN reservation ON field.field_id = reservation.field_id
        WHERE reservation.reservation_id = NEW.reservation_id
    )
    WHERE reservation_id = NEW.reservation_id;

    UPDATE payments 
    SET paid = FALSE 
    WHERE reservation_id = NEW.reservation_id;

    RETURN NEW;
END;
$$;
 '   DROP FUNCTION public.update_payment();
       public          postgres    false                       1255    20087 +   updatefirstname(integer, character varying)    FUNCTION     �  CREATE FUNCTION public.updatefirstname(person_id_to_update integer, new_first_name character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    updated BOOLEAN;
BEGIN
    updated := false;

    UPDATE person
    SET first_name = new_first_name
    WHERE person_id = person_id_to_update;

    GET DIAGNOSTICS updated = ROW_COUNT;

    RETURN updated;
END;
$$;
 e   DROP FUNCTION public.updatefirstname(person_id_to_update integer, new_first_name character varying);
       public          postgres    false                       1255    20086 *   updatelastname(integer, character varying)    FUNCTION     �  CREATE FUNCTION public.updatelastname(person_id_to_update integer, new_last_name character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    updated BOOLEAN;
BEGIN
    updated := false;

    UPDATE person
    SET last_name = new_last_name
    WHERE person_id = person_id_to_update;

    GET DIAGNOSTICS updated = ROW_COUNT;

    RETURN updated;
END;
$$;
 c   DROP FUNCTION public.updatelastname(person_id_to_update integer, new_last_name character varying);
       public          postgres    false            �            1259    19768    _groups    TABLE     e   CREATE TABLE public._groups (
    group_id integer NOT NULL,
    group_name character varying(30)
);
    DROP TABLE public._groups;
       public         heap    postgres    false            �            1259    19767    _groups_group_id_seq    SEQUENCE     �   CREATE SEQUENCE public._groups_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public._groups_group_id_seq;
       public          postgres    false    229            �           0    0    _groups_group_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public._groups_group_id_seq OWNED BY public._groups.group_id;
          public          postgres    false    228            �            1259    19960    _user    TABLE     �   CREATE TABLE public._user (
    user_id integer NOT NULL,
    email character varying(50),
    user_password character varying(50)
);
    DROP TABLE public._user;
       public         heap    postgres    false            �            1259    19959    _user_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public._user_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public._user_user_id_seq;
       public          postgres    false    239            �           0    0    _user_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public._user_user_id_seq OWNED BY public._user.user_id;
          public          postgres    false    238            �            1259    19754    address    TABLE     �   CREATE TABLE public.address (
    address_id integer NOT NULL,
    country character varying(50),
    county character varying(50),
    city character varying(50),
    address_details character varying(100)
);
    DROP TABLE public.address;
       public         heap    postgres    false            �            1259    19753    address_address_id_seq    SEQUENCE     �   CREATE SEQUENCE public.address_address_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.address_address_id_seq;
       public          postgres    false    225            �           0    0    address_address_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.address_address_id_seq OWNED BY public.address.address_id;
          public          postgres    false    224            �            1259    19717    field    TABLE     j   CREATE TABLE public.field (
    field_id integer NOT NULL,
    address_id integer,
    price_hour real
);
    DROP TABLE public.field;
       public         heap    postgres    false            �            1259    19733    inside_field    TABLE     �   CREATE TABLE public.inside_field (
    field_id integer NOT NULL,
    ventilation boolean,
    floor_material character varying(100)
);
     DROP TABLE public.inside_field;
       public         heap    postgres    false            �            1259    19723    outside_field    TABLE     �   CREATE TABLE public.outside_field (
    field_id integer NOT NULL,
    surface_nature character varying(100),
    night_lights boolean
);
 !   DROP TABLE public.outside_field;
       public         heap    postgres    false            �            1259    19747    sport    TABLE     |   CREATE TABLE public.sport (
    sport_id integer NOT NULL,
    sport_name character varying(40),
    max_players integer
);
    DROP TABLE public.sport;
       public         heap    postgres    false            �            1259    19743    sport_field    TABLE     P   CREATE TABLE public.sport_field (
    field_id integer,
    sport_id integer
);
    DROP TABLE public.sport_field;
       public         heap    postgres    false            �            1259    20007    allfielddetails    VIEW     �  CREATE VIEW public.allfielddetails AS
 SELECT f.field_id,
    a.country,
    a.county,
    a.city,
    a.address_details,
    f.price_hour,
    s.sport_name,
    s.max_players,
        CASE
            WHEN (i.field_id IS NOT NULL) THEN 'indoor'::character varying
            ELSE 'outdoor'::character varying
        END AS field_type,
        CASE
            WHEN (i.field_id IS NOT NULL) THEN i.ventilation
            ELSE of.night_lights
        END AS ventilation_lights,
        CASE
            WHEN (i.field_id IS NOT NULL) THEN i.floor_material
            ELSE (of.surface_nature)::character varying
        END AS floor_surface
   FROM (((((public.field f
     LEFT JOIN public.inside_field i ON ((i.field_id = f.field_id)))
     LEFT JOIN public.outside_field of ON ((of.field_id = f.field_id)))
     LEFT JOIN public.address a ON ((a.address_id = f.address_id)))
     LEFT JOIN public.sport_field sf ON ((sf.field_id = f.field_id)))
     LEFT JOIN public.sport s ON ((s.sport_id = sf.sport_id)));
 "   DROP VIEW public.allfielddetails;
       public          postgres    false    225    225    225    225    225    223    223    223    221    221    220    220    220    219    219    219    218    218    218            �            1259    19796    closed_days    TABLE     b   CREATE TABLE public.closed_days (
    field_id integer,
    holiday_date date,
    reason text
);
    DROP TABLE public.closed_days;
       public         heap    postgres    false            �            1259    19785    feedback    TABLE     �   CREATE TABLE public.feedback (
    feedback_id integer NOT NULL,
    player_id integer,
    feedback_text character varying(2000),
    feedback_date date
);
    DROP TABLE public.feedback;
       public         heap    postgres    false            �            1259    19784    feedback_feedback_id_seq    SEQUENCE     �   CREATE SEQUENCE public.feedback_feedback_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.feedback_feedback_id_seq;
       public          postgres    false    234            �           0    0    feedback_feedback_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.feedback_feedback_id_seq OWNED BY public.feedback.feedback_id;
          public          postgres    false    233            �            1259    19716    field_field_id_seq    SEQUENCE     �   CREATE SEQUENCE public.field_field_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.field_field_id_seq;
       public          postgres    false    218            �           0    0    field_field_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.field_field_id_seq OWNED BY public.field.field_id;
          public          postgres    false    217            �            1259    19774    members    TABLE     g   CREATE TABLE public.members (
    group_id integer,
    member_id integer,
    status_admin boolean
);
    DROP TABLE public.members;
       public         heap    postgres    false            �            1259    20035    group_players_view    VIEW     0  CREATE VIEW public.group_players_view AS
 SELECT g.group_name,
    s.max_players,
    count(m.member_id) AS group_nr_of_players,
        CASE
            WHEN ((s.max_players > count(m.member_id)) OR (s.max_players IS NULL)) THEN true
            ELSE false
        END AS available
   FROM (((public._groups g
     LEFT JOIN public.members m ON ((g.group_id = m.group_id)))
     LEFT JOIN public.sport_field sf ON ((g.group_id = sf.field_id)))
     LEFT JOIN public.sport s ON ((sf.sport_id = s.sport_id)))
  GROUP BY g.group_id, g.group_name, s.max_players;
 %   DROP VIEW public.group_players_view;
       public          postgres    false    223    229    229    230    230    221    221    223            �            1259    19953    person    TABLE       CREATE TABLE public.person (
    person_id integer NOT NULL,
    last_name character varying(100),
    first_name character varying(100),
    date_of_birth date,
    status integer,
    gender public.gender_enum1,
    CONSTRAINT date_of_birth CHECK ((date_of_birth < CURRENT_DATE))
);
    DROP TABLE public.person;
       public         heap    postgres    false    933            �            1259    20025    mygroupdetails    VIEW     �  CREATE VIEW public.mygroupdetails AS
 SELECT g.group_name,
    p.person_id,
    p.last_name,
    p.first_name,
        CASE
            WHEN (m.status_admin = true) THEN 'LEADER'::character varying
            ELSE 'MEMBER'::character varying
        END AS status
   FROM ((public.members m
     LEFT JOIN public._groups g ON ((m.group_id = g.group_id)))
     LEFT JOIN public.person p ON ((p.person_id = m.member_id)));
 !   DROP VIEW public.mygroupdetails;
       public          postgres    false    229    237    237    237    230    230    230    229            �            1259    19778    payments    TABLE     �   CREATE TABLE public.payments (
    payment_id integer NOT NULL,
    reservation_id integer,
    payment_amount integer,
    payment_date date,
    payment_time time without time zone,
    paid boolean
);
    DROP TABLE public.payments;
       public         heap    postgres    false            �            1259    19777    payments_payment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.payments_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.payments_payment_id_seq;
       public          postgres    false    232            �           0    0    payments_payment_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.payments_payment_id_seq OWNED BY public.payments.payment_id;
          public          postgres    false    231            �            1259    19952    person_person_id_seq    SEQUENCE     �   CREATE SEQUENCE public.person_person_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.person_person_id_seq;
       public          postgres    false    237            �           0    0    person_person_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.person_person_id_seq OWNED BY public.person.person_id;
          public          postgres    false    236            �            1259    19710    person_status    TABLE     a   CREATE TABLE public.person_status (
    id integer NOT NULL,
    status character varying(30)
);
 !   DROP TABLE public.person_status;
       public         heap    postgres    false            �            1259    19709    person_status_id_seq    SEQUENCE     �   CREATE SEQUENCE public.person_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.person_status_id_seq;
       public          postgres    false    216            �           0    0    person_status_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.person_status_id_seq OWNED BY public.person_status.id;
          public          postgres    false    215            �            1259    20075    persondetails    VIEW     �  CREATE VIEW public.persondetails AS
 SELECT p.person_id,
    p.last_name,
    p.first_name,
    p.date_of_birth,
    p.gender,
        CASE
            WHEN (p.status = 1) THEN 'PLAYER'::character varying
            ELSE 'ADMIN'::character varying
        END AS person_status,
    u.email,
    u.user_password
   FROM ((public.person p
     LEFT JOIN public.person_status ps ON ((ps.id = p.person_id)))
     LEFT JOIN public._user u ON ((u.user_id = p.person_id)));
     DROP VIEW public.persondetails;
       public          postgres    false    239    237    237    237    216    239    239    237    237    237    933            �            1259    19761    reservation    TABLE     �  CREATE TABLE public.reservation (
    reservation_id integer NOT NULL,
    field_id integer,
    reservation_date date,
    start_time time without time zone,
    end_time time without time zone,
    reserved_by integer,
    CONSTRAINT end_time CHECK ((end_time > start_time)),
    CONSTRAINT reservation_date CHECK ((reservation_date >= CURRENT_DATE)),
    CONSTRAINT start_time CHECK ((start_time < end_time))
);
    DROP TABLE public.reservation;
       public         heap    postgres    false            �            1259    20063    reservation_details_view    VIEW     �  CREATE VIEW public.reservation_details_view AS
 SELECT r.reservation_id,
    g.group_name,
    r.reservation_date,
    ((to_char((r.start_time)::interval, 'HH:MI'::text) || '-'::text) || to_char((r.end_time)::interval, 'HH:MI'::text)) AS hours,
    s.sport_name,
    a.country,
    a.city,
    a.address_details,
    count(m.member_id) AS group_nr_of_players,
        CASE
            WHEN ((s.max_players > count(m.member_id)) OR (s.max_players IS NULL)) THEN true
            ELSE false
        END AS available
   FROM ((((((public.reservation r
     JOIN public._groups g ON ((r.reserved_by = g.group_id)))
     LEFT JOIN public.members m ON ((g.group_id = m.group_id)))
     JOIN public.sport_field sf ON ((r.field_id = sf.field_id)))
     JOIN public.sport s ON ((sf.sport_id = s.sport_id)))
     JOIN public.field f ON ((sf.field_id = f.field_id)))
     JOIN public.address a ON ((f.address_id = a.address_id)))
  GROUP BY r.reservation_id, g.group_name, r.reservation_date, ((to_char((r.start_time)::interval, 'HH:MI'::text) || '-'::text) || to_char((r.end_time)::interval, 'HH:MI'::text)), s.sport_name, a.country, a.city, a.address_details, s.max_players;
 +   DROP VIEW public.reservation_details_view;
       public          postgres    false    225    218    218    221    221    223    223    223    225    227    225    225    227    227    227    227    230    230    229    229    227            �            1259    19760    reservation_reservation_id_seq    SEQUENCE     �   CREATE SEQUENCE public.reservation_reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.reservation_reservation_id_seq;
       public          postgres    false    227            �           0    0    reservation_reservation_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.reservation_reservation_id_seq OWNED BY public.reservation.reservation_id;
          public          postgres    false    226            �            1259    19746    sport_sport_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sport_sport_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.sport_sport_id_seq;
       public          postgres    false    223            �           0    0    sport_sport_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.sport_sport_id_seq OWNED BY public.sport.sport_id;
          public          postgres    false    222            �            1259    19987 	   timetable    TABLE     �   CREATE TABLE public.timetable (
    day_of_week public.days,
    start_time time without time zone,
    end_time time without time zone
);
    DROP TABLE public.timetable;
       public         heap    postgres    false    882            �           2604    19771    _groups group_id    DEFAULT     t   ALTER TABLE ONLY public._groups ALTER COLUMN group_id SET DEFAULT nextval('public._groups_group_id_seq'::regclass);
 ?   ALTER TABLE public._groups ALTER COLUMN group_id DROP DEFAULT;
       public          postgres    false    229    228    229            �           2604    19963    _user user_id    DEFAULT     n   ALTER TABLE ONLY public._user ALTER COLUMN user_id SET DEFAULT nextval('public._user_user_id_seq'::regclass);
 <   ALTER TABLE public._user ALTER COLUMN user_id DROP DEFAULT;
       public          postgres    false    239    238    239            �           2604    19757    address address_id    DEFAULT     x   ALTER TABLE ONLY public.address ALTER COLUMN address_id SET DEFAULT nextval('public.address_address_id_seq'::regclass);
 A   ALTER TABLE public.address ALTER COLUMN address_id DROP DEFAULT;
       public          postgres    false    224    225    225            �           2604    19788    feedback feedback_id    DEFAULT     |   ALTER TABLE ONLY public.feedback ALTER COLUMN feedback_id SET DEFAULT nextval('public.feedback_feedback_id_seq'::regclass);
 C   ALTER TABLE public.feedback ALTER COLUMN feedback_id DROP DEFAULT;
       public          postgres    false    233    234    234            �           2604    19720    field field_id    DEFAULT     p   ALTER TABLE ONLY public.field ALTER COLUMN field_id SET DEFAULT nextval('public.field_field_id_seq'::regclass);
 =   ALTER TABLE public.field ALTER COLUMN field_id DROP DEFAULT;
       public          postgres    false    218    217    218            �           2604    19781    payments payment_id    DEFAULT     z   ALTER TABLE ONLY public.payments ALTER COLUMN payment_id SET DEFAULT nextval('public.payments_payment_id_seq'::regclass);
 B   ALTER TABLE public.payments ALTER COLUMN payment_id DROP DEFAULT;
       public          postgres    false    232    231    232            �           2604    19956    person person_id    DEFAULT     t   ALTER TABLE ONLY public.person ALTER COLUMN person_id SET DEFAULT nextval('public.person_person_id_seq'::regclass);
 ?   ALTER TABLE public.person ALTER COLUMN person_id DROP DEFAULT;
       public          postgres    false    236    237    237            �           2604    19713    person_status id    DEFAULT     t   ALTER TABLE ONLY public.person_status ALTER COLUMN id SET DEFAULT nextval('public.person_status_id_seq'::regclass);
 ?   ALTER TABLE public.person_status ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    19764    reservation reservation_id    DEFAULT     �   ALTER TABLE ONLY public.reservation ALTER COLUMN reservation_id SET DEFAULT nextval('public.reservation_reservation_id_seq'::regclass);
 I   ALTER TABLE public.reservation ALTER COLUMN reservation_id DROP DEFAULT;
       public          postgres    false    227    226    227            �           2604    19750    sport sport_id    DEFAULT     p   ALTER TABLE ONLY public.sport ALTER COLUMN sport_id SET DEFAULT nextval('public.sport_sport_id_seq'::regclass);
 =   ALTER TABLE public.sport ALTER COLUMN sport_id DROP DEFAULT;
       public          postgres    false    223    222    223            �          0    19768    _groups 
   TABLE DATA           7   COPY public._groups (group_id, group_name) FROM stdin;
    public          postgres    false    229   �       �          0    19960    _user 
   TABLE DATA           >   COPY public._user (user_id, email, user_password) FROM stdin;
    public          postgres    false    239   ��       �          0    19754    address 
   TABLE DATA           U   COPY public.address (address_id, country, county, city, address_details) FROM stdin;
    public          postgres    false    225   ��       �          0    19796    closed_days 
   TABLE DATA           E   COPY public.closed_days (field_id, holiday_date, reason) FROM stdin;
    public          postgres    false    235   `�       �          0    19785    feedback 
   TABLE DATA           X   COPY public.feedback (feedback_id, player_id, feedback_text, feedback_date) FROM stdin;
    public          postgres    false    234   }�       �          0    19717    field 
   TABLE DATA           A   COPY public.field (field_id, address_id, price_hour) FROM stdin;
    public          postgres    false    218   ��       �          0    19733    inside_field 
   TABLE DATA           M   COPY public.inside_field (field_id, ventilation, floor_material) FROM stdin;
    public          postgres    false    220   ��       �          0    19774    members 
   TABLE DATA           D   COPY public.members (group_id, member_id, status_admin) FROM stdin;
    public          postgres    false    230   H�       �          0    19723    outside_field 
   TABLE DATA           O   COPY public.outside_field (field_id, surface_nature, night_lights) FROM stdin;
    public          postgres    false    219   ��       �          0    19778    payments 
   TABLE DATA           p   COPY public.payments (payment_id, reservation_id, payment_amount, payment_date, payment_time, paid) FROM stdin;
    public          postgres    false    232   �       �          0    19953    person 
   TABLE DATA           a   COPY public.person (person_id, last_name, first_name, date_of_birth, status, gender) FROM stdin;
    public          postgres    false    237   /�       �          0    19710    person_status 
   TABLE DATA           3   COPY public.person_status (id, status) FROM stdin;
    public          postgres    false    216   ��       �          0    19761    reservation 
   TABLE DATA           t   COPY public.reservation (reservation_id, field_id, reservation_date, start_time, end_time, reserved_by) FROM stdin;
    public          postgres    false    227   �       �          0    19747    sport 
   TABLE DATA           B   COPY public.sport (sport_id, sport_name, max_players) FROM stdin;
    public          postgres    false    223   ��       �          0    19743    sport_field 
   TABLE DATA           9   COPY public.sport_field (field_id, sport_id) FROM stdin;
    public          postgres    false    221   �       �          0    19987 	   timetable 
   TABLE DATA           F   COPY public.timetable (day_of_week, start_time, end_time) FROM stdin;
    public          postgres    false    240   O�       �           0    0    _groups_group_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public._groups_group_id_seq', 39, true);
          public          postgres    false    228            �           0    0    _user_user_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public._user_user_id_seq', 30, true);
          public          postgres    false    238            �           0    0    address_address_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.address_address_id_seq', 16, true);
          public          postgres    false    224            �           0    0    feedback_feedback_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.feedback_feedback_id_seq', 1, false);
          public          postgres    false    233            �           0    0    field_field_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.field_field_id_seq', 39, true);
          public          postgres    false    217            �           0    0    payments_payment_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.payments_payment_id_seq', 1, false);
          public          postgres    false    231            �           0    0    person_person_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.person_person_id_seq', 30, true);
          public          postgres    false    236            �           0    0    person_status_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.person_status_id_seq', 2, true);
          public          postgres    false    215            �           0    0    reservation_reservation_id_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.reservation_reservation_id_seq', 43, true);
          public          postgres    false    226            �           0    0    sport_sport_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.sport_sport_id_seq', 23, true);
          public          postgres    false    222            �           2606    19773    _groups _groups_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public._groups
    ADD CONSTRAINT _groups_pkey PRIMARY KEY (group_id);
 >   ALTER TABLE ONLY public._groups DROP CONSTRAINT _groups_pkey;
       public            postgres    false    229            �           2606    19965    _user _user_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public._user
    ADD CONSTRAINT _user_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public._user DROP CONSTRAINT _user_pkey;
       public            postgres    false    239            �           2606    19759    address address_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);
 >   ALTER TABLE ONLY public.address DROP CONSTRAINT address_pkey;
       public            postgres    false    225            �           2606    19792    feedback feedback_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (feedback_id);
 @   ALTER TABLE ONLY public.feedback DROP CONSTRAINT feedback_pkey;
       public            postgres    false    234            �           2606    19722    field field_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.field
    ADD CONSTRAINT field_pkey PRIMARY KEY (field_id);
 :   ALTER TABLE ONLY public.field DROP CONSTRAINT field_pkey;
       public            postgres    false    218            �           2606    19737    inside_field inside_field_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.inside_field
    ADD CONSTRAINT inside_field_pkey PRIMARY KEY (field_id);
 H   ALTER TABLE ONLY public.inside_field DROP CONSTRAINT inside_field_pkey;
       public            postgres    false    220            �           2606    19727     outside_field outside_field_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.outside_field
    ADD CONSTRAINT outside_field_pkey PRIMARY KEY (field_id);
 J   ALTER TABLE ONLY public.outside_field DROP CONSTRAINT outside_field_pkey;
       public            postgres    false    219            �           2606    19783    payments payments_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (payment_id);
 @   ALTER TABLE ONLY public.payments DROP CONSTRAINT payments_pkey;
       public            postgres    false    232            �           2606    19958    person person_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);
 <   ALTER TABLE ONLY public.person DROP CONSTRAINT person_pkey;
       public            postgres    false    237            �           2606    19715     person_status person_status_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.person_status
    ADD CONSTRAINT person_status_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.person_status DROP CONSTRAINT person_status_pkey;
       public            postgres    false    216            �           2606    19766    reservation reservation_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (reservation_id);
 F   ALTER TABLE ONLY public.reservation DROP CONSTRAINT reservation_pkey;
       public            postgres    false    227            �           2606    19752    sport sport_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.sport
    ADD CONSTRAINT sport_pkey PRIMARY KEY (sport_id);
 :   ALTER TABLE ONLY public.sport DROP CONSTRAINT sport_pkey;
       public            postgres    false    223            �           2620    19875 (   reservation check_player_allowed_trigger    TRIGGER     �   CREATE TRIGGER check_player_allowed_trigger BEFORE INSERT ON public.reservation FOR EACH STATEMENT EXECUTE FUNCTION public.check_players();
 A   DROP TRIGGER check_player_allowed_trigger ON public.reservation;
       public          postgres    false    227    246            �           2620    19876    reservation update_payments    TRIGGER     y   CREATE TRIGGER update_payments AFTER INSERT ON public.reservation FOR EACH ROW EXECUTE FUNCTION public.update_payment();
 4   DROP TRIGGER update_payments ON public.reservation;
       public          postgres    false    227    249            �           2606    19816    field fk_address    FK CONSTRAINT     |   ALTER TABLE ONLY public.field
    ADD CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES public.address(address_id);
 :   ALTER TABLE ONLY public.field DROP CONSTRAINT fk_address;
       public          postgres    false    225    218    4820            �           2606    19841    closed_days fk_closed_days    FK CONSTRAINT     �   ALTER TABLE ONLY public.closed_days
    ADD CONSTRAINT fk_closed_days FOREIGN KEY (field_id) REFERENCES public.field(field_id);
 D   ALTER TABLE ONLY public.closed_days DROP CONSTRAINT fk_closed_days;
       public          postgres    false    4812    235    218            �           2606    19982    feedback fk_feedback    FK CONSTRAINT     }   ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT fk_feedback FOREIGN KEY (player_id) REFERENCES public.person(person_id);
 >   ALTER TABLE ONLY public.feedback DROP CONSTRAINT fk_feedback;
       public          postgres    false    237    4830    234            �           2606    19806    sport_field fk_field    FK CONSTRAINT     z   ALTER TABLE ONLY public.sport_field
    ADD CONSTRAINT fk_field FOREIGN KEY (field_id) REFERENCES public.field(field_id);
 >   ALTER TABLE ONLY public.sport_field DROP CONSTRAINT fk_field;
       public          postgres    false    4812    218    221            �           2606    19826    reservation fk_group    FK CONSTRAINT        ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk_group FOREIGN KEY (reserved_by) REFERENCES public._groups(group_id);
 >   ALTER TABLE ONLY public.reservation DROP CONSTRAINT fk_group;
       public          postgres    false    229    227    4824            �           2606    19851    members fk_group    FK CONSTRAINT     x   ALTER TABLE ONLY public.members
    ADD CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES public._groups(group_id);
 :   ALTER TABLE ONLY public.members DROP CONSTRAINT fk_group;
       public          postgres    false    4824    229    230            �           2606    19971    members fk_members    FK CONSTRAINT     {   ALTER TABLE ONLY public.members
    ADD CONSTRAINT fk_members FOREIGN KEY (member_id) REFERENCES public.person(person_id);
 <   ALTER TABLE ONLY public.members DROP CONSTRAINT fk_members;
       public          postgres    false    230    4830    237            �           2606    19966    person fk_person_status    FK CONSTRAINT     }   ALTER TABLE ONLY public.person
    ADD CONSTRAINT fk_person_status FOREIGN KEY (status) REFERENCES public.person_status(id);
 A   ALTER TABLE ONLY public.person DROP CONSTRAINT fk_person_status;
       public          postgres    false    237    4810    216            �           2606    19831    payments fk_reservation_payment    FK CONSTRAINT     �   ALTER TABLE ONLY public.payments
    ADD CONSTRAINT fk_reservation_payment FOREIGN KEY (reservation_id) REFERENCES public.reservation(reservation_id);
 I   ALTER TABLE ONLY public.payments DROP CONSTRAINT fk_reservation_payment;
       public          postgres    false    232    4822    227            �           2606    19821    reservation fk_reserved_field    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk_reserved_field FOREIGN KEY (field_id) REFERENCES public.field(field_id);
 G   ALTER TABLE ONLY public.reservation DROP CONSTRAINT fk_reserved_field;
       public          postgres    false    227    4812    218            �           2606    19811    sport_field fk_sport    FK CONSTRAINT     z   ALTER TABLE ONLY public.sport_field
    ADD CONSTRAINT fk_sport FOREIGN KEY (sport_id) REFERENCES public.sport(sport_id);
 >   ALTER TABLE ONLY public.sport_field DROP CONSTRAINT fk_sport;
       public          postgres    false    223    221    4818            �           2606    19976    person fk_user_id    FK CONSTRAINT     w   ALTER TABLE ONLY public.person
    ADD CONSTRAINT fk_user_id FOREIGN KEY (person_id) REFERENCES public._user(user_id);
 ;   ALTER TABLE ONLY public.person DROP CONSTRAINT fk_user_id;
       public          postgres    false    4832    237    239            �           2606    19738 '   inside_field inside_field_field_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.inside_field
    ADD CONSTRAINT inside_field_field_id_fkey FOREIGN KEY (field_id) REFERENCES public.field(field_id);
 Q   ALTER TABLE ONLY public.inside_field DROP CONSTRAINT inside_field_field_id_fkey;
       public          postgres    false    4812    218    220            �           2606    19728 )   outside_field outside_field_field_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.outside_field
    ADD CONSTRAINT outside_field_field_id_fkey FOREIGN KEY (field_id) REFERENCES public.field(field_id);
 S   ALTER TABLE ONLY public.outside_field DROP CONSTRAINT outside_field_field_id_fkey;
       public          postgres    false    4812    219    218            �   h   x�32�NMT����+�22��K-K-RH�,KU(-�2��.-H-��/K�26�L�IM�/.�26@Uh�el�P	���q� �<sΐ��\�BKN��|�1z\\\ *      �     x�m�_o� ş�à���-ͪViթ�6u��rc0\��������h�%����s.���X��*��Z;@�r�HzA9�Ž
�EtJ#̔Z�,J��6�*���E�h^K~��D�:%���C�Y�n}��yA����H �� ��>cuc�E�j�� (�o|�9��RV��4�.X��L�'�J��h��+��%.W#�����Cg��j��	)�������G������g|;���rK9'G���rhOc�{y��כ�~���g$V�-�86�y������鎟�zF�t��Gߘ+t��__��vs���3��I���#���y�;B���F�0�� W�����[�?�<WIuE�p*8�������n����{,�ӱ��)�gH�%��	3�ӧ��4)S2PP���Ǥ�}��4]O��d)��?˦��bIŜ�C���Q�RF*���,x�%����</b2�:2���R�����I6j/��ل�C�*���ނS@1J�T	@      �   �   x���=�@�k�s !j	���X��$�Y�����BKBb�4/�x_^>
�S�r��+��O�W��{��x#�vcO1��@���(7it�yk�(Z
���砧�u�3�a,�N� ��Og�Ŷq	w�����-m0!�t��`��
���P�0�G	��Z��$�k  _��q      �      x������ � �      �      x������ � �      �   O   x�5��� E�o�P�]���h��p���]�ך�,M����&9a0L0���Xܣ^�!Rl��,%�4Oi��GU?�_�      �   ?   x�3��,��M,�I�Q(�ϩ,-J-�H�KUH�KQ(�̫��26�L�,O,I-�26'�:F��� �"�      �   \   x�5���@Cϡg��ًg+���޼��`�[\a�8���3h�D����������Gγ�m�'o���l|�Z��5���sw"���-      �   N   x�32�,��+�H-�L�,�22�,�IL�L�2��L/J,.
��16D�A� Y�p�	�e�Y���4��Ɗ���� {B!�      �      x������ � �      �   �  x�}Rъ�0|^���V�c�1����pGrP8��8/��!ǁ�}W��;�r`䇙���]��K��PR�BjQK@�Q)x���i��h?0�U;Z���R�dJVh`gN��3�D�8;Ka6�����e0����E6�L�d̲����4��hE�������bFi5���k8Nti���o���(�W�<|'F�>�.3"�or<�4����_&�6i˺�װg�����ʽg�]B�1Y ����^D!����!Z�f<G�����-c��ۘv�d��������<�_	�yt	
/#�V��'��|�!����ɘ��"�P#$
U����i�3֖�<^��*��}e��9D8����rs~����"|wEJ���>8��,�uv�o�SJ�z�p����X,��RU�?n���      �      x�3�,�I�L-�2�LL�������� JV�      �   �   x�e��1�7��E�bc��뿎\�#���]0M<���P;���S�O���;x��V:KY���X���T�*���R)��*��[�(U� �ж�Zڢ��B�{�
e l����ϱLn��	N� /��;�      �   Z   x�3��t��/IJ���42�2��tJ,�N�pp����esqr����VB$�F��噹��y�1~\FƜ�y)��b���� �(      �   <   x�˱�0B�Z��@g˻d�9Ѽ�s�"<�vP���e�rVɖ�AB�+��w| �      �   M   x����KI��4��20 "N##�+�4��LxjJ.����"RnE��%�KJ�P�`R���34�J�Mg��qqq 
BW�     