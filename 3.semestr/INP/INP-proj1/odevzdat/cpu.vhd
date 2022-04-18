-- cpu.vhd: Simple 8-bit CPU (BrainLove interpreter)
-- Copyright (C) 2021 Brno University of Technology,
--                    Faculty of Information Technology
-- Author(s): DOPLNIT
--

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

-- ----------------------------------------------------------------------------
--                        Entity declaration
-- ----------------------------------------------------------------------------
entity cpu is
 port (
   CLK   : in std_logic;  -- hodinovy signal
   RESET : in std_logic;  -- asynchronni reset procesoru
   EN    : in std_logic;  -- povoleni cinnosti procesoru
 
   -- synchronni pamet ROM
   CODE_ADDR : out std_logic_vector(11 downto 0); -- adresa do pameti
   CODE_DATA : in std_logic_vector(7 downto 0);   -- CODE_DATA <- rom[CODE_ADDR] pokud CODE_EN='1'
   CODE_EN   : out std_logic;                     -- povoleni cinnosti
   
   -- synchronni pamet RAM
   DATA_ADDR  : out std_logic_vector(9 downto 0); -- adresa do pameti
   DATA_WDATA : out std_logic_vector(7 downto 0); -- ram[DATA_ADDR] <- DATA_WDATA pokud DATA_EN='1'
   DATA_RDATA : in std_logic_vector(7 downto 0);  -- DATA_RDATA <- ram[DATA_ADDR] pokud DATA_EN='1'
   DATA_WREN  : out std_logic;                    -- cteni z pameti (DATA_WREN='0') / zapis do pameti (DATA_WREN='1')
   DATA_EN    : out std_logic;                    -- povoleni cinnosti
   
   -- vstupni port
   IN_DATA   : in std_logic_vector(7 downto 0);   -- IN_DATA obsahuje stisknuty znak klavesnice pokud IN_VLD='1' a IN_REQ='1'
   IN_VLD    : in std_logic;                      -- data platna pokud IN_VLD='1'
   IN_REQ    : out std_logic;                     -- pozadavek na vstup dat z klavesnice
   
   -- vystupni port
   OUT_DATA : out  std_logic_vector(7 downto 0);  -- zapisovana data
   OUT_BUSY : in std_logic;                       -- pokud OUT_BUSY='1', LCD je zaneprazdnen, nelze zapisovat,  OUT_WREN musi byt '0'
   OUT_WREN : out std_logic                       -- LCD <- OUT_DATA pokud OUT_WE='1' a OUT_BUSY='0'
 );
end cpu;


-- ----------------------------------------------------------------------------
--                      Architecture declaration
-- ----------------------------------------------------------------------------
architecture behavioral of cpu is

-- PC
		signal pc_reg : std_logic_vector (11 downto 0);
		signal pc_inc : std_logic;
		signal pc_dec : std_logic;
-- PC

-- PTR
		signal ptr_reg : std_logic_vector (9 downto 0);
		signal ptr_inc : std_logic;
		signal ptr_dec : std_logic;
-- PTR
-- CNT
		signal cnt_reg : std_logic_vector (7 downto 0);
		signal cnt_inc : std_logic;
		signal cnt_dec : std_logic;
-- CNT

-- STATES
		type fsm_state is(
			s_start,
			s_fetch,
			s_decode,
			
			s_program_inc,
			s_program_dec,
			s_program_mx_inc,
			s_program_end_inc,
			s_program_mx_dec,
			s_program_end_dec,
			
			s_pointer_inc,
			s_pointer_dec,
			
			s_while_en_start,
			s_while_en_end,
			s_while_start,
			s_while_loop_start,
			s_while_loop_end,
			s_while_end,
			s_while_check_start,
			s_while_check_end,
			s_while_break,
			s_while_loop_if_start,
			s_while_loop_if_end,
			
			s_write,
			s_write_done,
			s_get,
			s_get_done,
												s_null
		);
		signal state : fsm_state := s_start;
		signal nState : fsm_state;
-- STATES

-- MUX
		signal mx_select : std_logic_vector (1 downto 0) := (others => '0');
		signal mx_output : std_logic_vector (7 downto 0) := (others => '0');
-- MUX
begin
-- PC
		pc: process (CLK, RESET, pc_inc, pc_dec) is
		begin
			if RESET = '1' then
				pc_reg <= (others => '0');
			elsif (CLK'event) and (CLK = '1') then
				if pc_inc = '1' then
					pc_reg <= pc_reg + 1;
				elsif pc_dec = '1' then
					pc_reg <= pc_reg-1;
				end if;
			end if;
		end process;
		CODE_ADDR <= pc_reg;
-- PC

-- PTR
		ptr: process (CLK, RESET, ptr_inc, ptr_dec) is
		begin
			if RESET = '1' then
				ptr_reg <= (others => '0');
			elsif (CLK'event) and (CLK = '1') then
				if ptr_inc = '1' then
					ptr_reg <= ptr_reg + 1;
				elsif ptr_dec = '1' then
					ptr_reg <= ptr_reg-1;
				end if;
			end if;
		end process;
		DATA_ADDR <= ptr_reg;
-- PTR

-- CNT
		cnt: process (CLK, RESET, cnt_inc, cnt_dec) is
		begin
			if RESET = '1' then
				cnt_reg <= (others => '0');
			elsif (CLK'event) and (CLK = '1') then
				if cnt_inc = '1' then
					cnt_reg <= cnt_reg + 1;
				elsif cnt_dec = '1' then
					cnt_reg <= cnt_reg-1;
				end if;
			end if;
		end process;
-- CNT

-- MUX
		mux: process (CLK, RESET, mx_select) is
		begin
			if RESET = '1' then
				mx_output <= (others => '0');
			elsif (CLK'event) and (CLK = '1') then
				case mx_select is
					when "00" =>
								mx_output <= IN_DATA;
					when "01" =>
								mx_output <= DATA_RDATA + 1;
					when "10" =>
								mx_output <= DATA_RDATA - 1;
					when others =>
								mx_output <= (others => '0');
				end case;
			end if;
		end process;
		DATA_WDATA <= mx_output;
-- MUX

-- FSM
		next_state_logic: process (CLK, RESET, EN) is
		begin
			if RESET = '1' then
				state <= s_start;
			elsif (CLK'event) and (CLK = '1') then
				if EN = '1' then
					state <= nState;
				end if;
			end if;
		end process;
		
		fsm: process (state, OUT_BUSY, IN_VLD, CODE_DATA, DATA_RDATA, cnt_reg) is
		begin
				-- initialization
				pc_inc <= '0';
				pc_dec <= '0';
				ptr_inc <= '0';
				ptr_dec <= '0';
				cnt_inc <= '0';
				cnt_dec <= '0';
				
				CODE_EN <= '0';
				DATA_EN <= '0';
				DATA_WREN <= '0';
				IN_REQ <= '0';
				OUT_WREN <= '0';
				
				mx_select <= "00";

				case state is
						when s_start =>
							nState <= s_fetch;
						when s_fetch =>
							CODE_EN <= '1';
							nState <= s_decode;
						when s_decode =>
							case CODE_DATA is
								when x"3E" =>
									nState <= s_pointer_inc;
								when x"3C" =>
									nState <= s_pointer_dec;
								when x"2B" =>
									nState <= s_program_inc;
								when x"2D" =>
									nState <= s_program_dec;
								when x"5B" =>
									nState <= s_while_start;
								when x"5D" =>
									nState <= s_while_end;
								when x"7E" =>
									nState <= s_while_break;
								when x"2E" =>
									nState <= s_write;
								when x"2C" =>
									nState <= s_get;
								when x"00" =>
									nState <= s_null;
								when others => 
									pc_inc <= '1';
									nState <= s_fetch;
							end case;
						-- ptr inc/dec
						when s_pointer_inc =>
								pc_inc <= '1';
								ptr_inc <= '1';
								nState <= s_fetch;
						when s_pointer_dec =>
								pc_inc <= '1';
								ptr_dec <= '1';
								nState <= s_fetch;
						
						-- program inc/dec
						when s_program_inc =>
								DATA_EN <= '1';
								DATA_WREN <= '0';
								nState <= s_program_mx_inc;
						when s_program_mx_inc =>
								mx_select <= "01"; 
								nState <= s_program_end_inc;
						when s_program_end_inc => 
								DATA_EN <= '1';
								DATA_WREN <= '1';
								pc_inc <= '1';
								nState <= s_fetch;
								
						when s_program_dec =>
								DATA_EN <= '1';
								DATA_WREN <= '0';
								nState <= s_program_mx_dec;
						when s_program_mx_dec =>
								mx_select <= "10"; 
								nState <= s_program_end_dec;
						when s_program_end_dec => 
								DATA_EN <= '1';
								DATA_WREN <= '1';
								pc_inc <= '1';
								nState <= s_fetch;
						
						-- write
						when s_write =>
								DATA_EN <= '1';
								DATA_WREN <= '0';
								nState <= s_write_done;
						when s_write_done =>
								if OUT_BUSY = '1' then
									DATA_EN <= '1';
									DATA_WREN <= '0';
									nState <= s_write_done;
								else
									OUT_WREN <= '1';
									OUT_DATA <= DATA_RDATA;
									pc_inc <= '1';
									nState <= s_fetch;
								end if;
						
						-- get
						when s_get =>
								IN_REQ <= '1';
								mx_select <= "00";
								nState <= s_get_done;
						when s_get_done =>
								if IN_VLD /= '1' then
									IN_REQ <= '1';
									mx_select <= "00";
									nState <= s_get_done;
								else
									DATA_EN <= '1';
									DATA_WREN <= '1';
									pc_inc <= '1';
									nState <= s_fetch;
								end if;
						
						-- while start
						when s_while_start =>
								pc_inc <= '1';
								DATA_EN <= '1';
								DATA_WREN <= '0';
								nState <= s_while_check_start;
						when s_while_check_start =>
								if DATA_RDATA = "00000000" then
									cnt_inc <= '1';
									nState <= s_while_en_start;
								else
									nState <= s_fetch;
								end if;	
						when s_while_en_start =>
								CODE_EN <= '1';
								DATA_EN <= '1';
								nState <= s_while_loop_if_start;
						when s_while_loop_if_start =>
								pc_inc <= '1';
								if CODE_DATA = x"5B" then
										cnt_inc <= '1';
								elsif CODE_DATA = x"5D" then
										cnt_dec <= '1';
								end if;
								nState <= s_while_loop_start; 
						when s_while_loop_start =>
								if cnt_reg /= "00000000" then
									nState <= s_while_en_start;
								else
									nState <= s_fetch;
								end if;	
								
						-- while end
						when s_while_end =>
								DATA_EN <= '1';
								DATA_WREN <= '0';
								nState <= s_while_check_end;
						when s_while_check_end =>
								if DATA_RDATA = "00000000" then
									pc_inc <= '1';
									nState <= s_fetch;
								else
									cnt_inc <= '1';
									pc_dec <= '1';
									nState <= s_while_en_end;
								end if;
						when s_while_en_end =>
								CODE_EN <= '1';
								DATA_EN <= '1';
								nState <= s_while_loop_if_end;
						when s_while_loop_if_end =>
								if CODE_DATA = x"5D" then
									cnt_inc <= '1';
								elsif CODE_DATA = x"5B" then
									cnt_dec <= '1';
								end if;
								nState <= s_while_loop_end;
						when s_while_loop_end =>
								if cnt_reg = "00000000" then
									pc_inc <= '1';
									nState <= s_fetch;
								else 
									pc_dec <= '1';
									nState <= s_while_en_end;
								end if;		
											
						-- while break
						when s_while_break =>
								pc_inc <= '1';
								cnt_inc <= '1';
								nState <= s_while_en_start;
								
						when s_null =>
								nState <= s_null;
						when others =>
								pc_inc <= '1';
								nState <= s_fetch;
				end case;
		end process;
-- FSM
end behavioral;
 
