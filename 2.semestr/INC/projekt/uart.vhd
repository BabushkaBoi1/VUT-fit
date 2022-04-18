-- uart.vhd: UART controller - receiving part
-- Author(s): xhajek51
--
library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

-------------------------------------------------
entity UART_RX is
port(	
    CLK: 	    in std_logic;
	RST: 	    in std_logic;
	DIN: 	    in std_logic;
	DOUT: 	    out std_logic_vector(7 downto 0);
	DOUT_VLD: 	out std_logic
);
end UART_RX;  

-------------------------------------------------
architecture behavioral of UART_RX is
signal cntClk 	: std_logic_vector(4 downto 0);
signal cntBit	: std_logic_vector(3 downto 0);
signal rxEn		: std_logic;
signal cntEnClk	: std_logic;
signal cmp 		: std_logic;
begin
    FSM: entity work.UART_FSM(behavioral)
    port map (
        CLK 	    => CLK,
        RST 	    => RST,
        DIN 	    => DIN,
        CNT_CLK	    => cntClk,
        CNT_BIT	 	=> cntBit,
		RX_EN 		=> rxEn,
		CNT_CLK_EN 	=> cntEnClk,
		DOUT_VLD 	=> DOUT_VLD
    );
	p_cntClk: process (CLK, RST) begin
		if RST = '1' or cntEnClk = '0' then
			cntClk <= "00000";
		elsif (CLK'event) and (CLK='1') then
			if cntEnClk ='1' then
				cntClk <= cntClk +1;
			end if;
			if cmp = '1' then
				cntClk <= "00000";
			end if;
		end if;
	end process;
	p_cmp: process (cntClk)
	begin
		if rxEn = '1' then
			if (cntClk >= "01111") then
				cmp <= '1';
			else
				cmp <= '0';
			end if;
		end if;
	end process;
	p_cntBit: process (CLK) begin
		if RST = '1' or cntEnClk = '0' then
			cntBit <= "0000";
		elsif (CLK'event) and (CLK='1') then
			if cmp = '1' and rxEn = '1' then
				cntBit <= cntBit + 1;
			end if;
		end if;
	end process;
	p_dout: process (CLK, RST) begin
		if RST = '1' or cntEnClk = '0' then
			DOUT <= "00000000";
		elsif (CLK'event) and (CLK='1') then
			if rxEn = '1' and cmp = '1' then
				case cntBit is
					when "0000" => DOUT(0) <= DIN;
					when "0001" => DOUT(1) <= DIN;
					when "0010" => DOUT(2) <= DIN;
					when "0011" => DOUT(3) <= DIN;
					when "0100" => DOUT(4) <= DIN;
					when "0101" => DOUT(5) <= DIN;
					when "0110" => DOUT(6) <= DIN;
					when "0111" => DOUT(7) <= DIN;
					when others => null;
				end case;
			end if;
		end if;
	end process;
end behavioral;
