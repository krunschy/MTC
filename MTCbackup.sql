PGDMP      :                 |            MTCdatabase    16.1    16.1     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16399    MTCdatabase    DATABASE     �   CREATE DATABASE "MTCdatabase" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United Kingdom.1252';
    DROP DATABASE "MTCdatabase";
                postgres    false            �            1259    16405    cards    TABLE     �   CREATE TABLE public.cards (
    cardid text NOT NULL,
    element text,
    cardtype text NOT NULL,
    name text NOT NULL,
    damage bigint NOT NULL,
    creaturetype text
);
    DROP TABLE public.cards;
       public         heap    postgres    false            �            1259    16656    packages    TABLE     �   CREATE TABLE public.packages (
    packageid integer NOT NULL,
    card1 text NOT NULL,
    card2 text NOT NULL,
    card3 text NOT NULL,
    card4 text NOT NULL
);
    DROP TABLE public.packages;
       public         heap    postgres    false            �            1259    16655    packages_packageID_seq    SEQUENCE     �   CREATE SEQUENCE public."packages_packageID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public."packages_packageID_seq";
       public          postgres    false    220            �           0    0    packages_packageID_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public."packages_packageID_seq" OWNED BY public.packages.packageid;
          public          postgres    false    219            �            1259    16474    stack    TABLE     �   CREATE TABLE public.stack (
    username text NOT NULL,
    cardid text NOT NULL,
    isintradeid text,
    isindeck boolean NOT NULL
);
    DROP TABLE public.stack;
       public         heap    postgres    false            �            1259    16464    trades    TABLE     �   CREATE TABLE public.trades (
    tradeid text NOT NULL,
    cardid text,
    reqdamage integer NOT NULL,
    reqelement text NOT NULL,
    reqtype text NOT NULL
);
    DROP TABLE public.trades;
       public         heap    postgres    false            �            1259    16400    users    TABLE     �   CREATE TABLE public.users (
    username text NOT NULL,
    password text NOT NULL,
    money bigint NOT NULL,
    elo bigint NOT NULL,
    bio text,
    image text,
    wins bigint NOT NULL,
    losses bigint NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            *           2604    16659    packages packageid    DEFAULT     z   ALTER TABLE ONLY public.packages ALTER COLUMN packageid SET DEFAULT nextval('public."packages_packageID_seq"'::regclass);
 A   ALTER TABLE public.packages ALTER COLUMN packageid DROP DEFAULT;
       public          postgres    false    219    220    220            �          0    16405    cards 
   TABLE DATA           V   COPY public.cards (cardid, element, cardtype, name, damage, creaturetype) FROM stdin;
    public          postgres    false    216   ,$       �          0    16656    packages 
   TABLE DATA           I   COPY public.packages (packageid, card1, card2, card3, card4) FROM stdin;
    public          postgres    false    220   �$       �          0    16474    stack 
   TABLE DATA           H   COPY public.stack (username, cardid, isintradeid, isindeck) FROM stdin;
    public          postgres    false    218   �$       �          0    16464    trades 
   TABLE DATA           Q   COPY public.trades (tradeid, cardid, reqdamage, reqelement, reqtype) FROM stdin;
    public          postgres    false    217   %       �          0    16400    users 
   TABLE DATA           Y   COPY public.users (username, password, money, elo, bio, image, wins, losses) FROM stdin;
    public          postgres    false    215   1%       �           0    0    packages_packageID_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public."packages_packageID_seq"', 11, true);
          public          postgres    false    219            .           2606    16503    cards Cards_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.cards
    ADD CONSTRAINT "Cards_pkey" PRIMARY KEY (cardid);
 <   ALTER TABLE ONLY public.cards DROP CONSTRAINT "Cards_pkey";
       public            postgres    false    216            4           2606    16663    packages packages_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_pkey PRIMARY KEY (packageid);
 @   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_pkey;
       public            postgres    false    220            2           2606    24593    stack stack_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT stack_pkey PRIMARY KEY (cardid);
 :   ALTER TABLE ONLY public.stack DROP CONSTRAINT stack_pkey;
       public            postgres    false    218            0           2606    16569    trades trades_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_pkey PRIMARY KEY (tradeid);
 <   ALTER TABLE ONLY public.trades DROP CONSTRAINT trades_pkey;
       public            postgres    false    217            ,           2606    16535    users users_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            9           2606    16664    packages packages_card1_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card1_fkey FOREIGN KEY (card1) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card1_fkey;
       public          postgres    false    216    220    4654            :           2606    16669    packages packages_card2_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card2_fkey FOREIGN KEY (card2) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card2_fkey;
       public          postgres    false    220    216    4654            ;           2606    16674    packages packages_card3_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card3_fkey FOREIGN KEY (card3) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card3_fkey;
       public          postgres    false    4654    216    220            <           2606    16679    packages packages_card4_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card4_fkey FOREIGN KEY (card4) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card4_fkey;
       public          postgres    false    216    220    4654            6           2606    16623    stack stack_CardID_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT "stack_CardID_fkey" FOREIGN KEY (cardid) REFERENCES public.cards(cardid);
 C   ALTER TABLE ONLY public.stack DROP CONSTRAINT "stack_CardID_fkey";
       public          postgres    false    216    218    4654            7           2606    16634    stack stack_IsInTradeID_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT "stack_IsInTradeID_fkey" FOREIGN KEY (isintradeid) REFERENCES public.trades(tradeid);
 H   ALTER TABLE ONLY public.stack DROP CONSTRAINT "stack_IsInTradeID_fkey";
       public          postgres    false    217    4656    218            8           2606    16608    stack stack_username_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.stack
    ADD CONSTRAINT stack_username_fkey FOREIGN KEY (username) REFERENCES public.users(username);
 C   ALTER TABLE ONLY public.stack DROP CONSTRAINT stack_username_fkey;
       public          postgres    false    4652    218    215            5           2606    16583    trades trades_cardid_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_cardid_fkey FOREIGN KEY (cardid) REFERENCES public.cards(cardid);
 C   ALTER TABLE ONLY public.trades DROP CONSTRAINT trades_cardid_fkey;
       public          postgres    false    216    217    4654            �   �   x�u�A�@E׿�1� � "�1�Ѕ65Vb��)�/PPc��5��ơ,�k��!�L�j�q4!y�W�r�DO�S(�q�6ןܶ���GX�����0سX��Xϛ��M�?I?�4����d���S�	|:
�6�^�.�G|���7��D0�rAD/0#Y�      �      x������ � �      �      x������ � �      �      x������ � �      �   h   x�u�1�@k�c"�g�w��hxE���	-J��f�	
R&a��}��e'�!�%��D�&�Ma�P6U����z�Ķ�l!� ��c������>����\%�     