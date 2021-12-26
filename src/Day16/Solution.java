package Day16;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private String binaryInput;
    private int counter;

    public static void main(String[] args) {
        Solution part = new Solution();
        part.toBinary();
        part.solution();
    }

    public void toBinary() {
        String input = "005532447836402684AC7AB3801A800021F0961146B1007A1147C89440294D005C12D2A7BC992D3F4E50C72CDF29EECFD0ACD5CC016962099194002CE31C5D3005F401296CAF4B656A46B2DE5588015C913D8653A3A001B9C3C93D7AC672F4FF78C136532E6E0007FCDFA975A3004B002E69EC4FD2D32CDF3FFDDAF01C91FCA7B41700263818025A00B48DEF3DFB89D26C3281A200F4C5AF57582527BC1890042DE00B4B324DBA4FAFCE473EF7CC0802B59DA28580212B3BD99A78C8004EC300761DC128EE40086C4F8E50F0C01882D0FE29900A01C01C2C96F38FCBB3E18C96F38FCBB3E1BCC57E2AA0154EDEC45096712A64A2520C6401A9E80213D98562653D98562612A06C0143CB03C529B5D9FD87CBA64F88CA439EC5BB299718023800D3CE7A935F9EA884F5EFAE9E10079125AF39E80212330F93EC7DAD7A9D5C4002A24A806A0062019B6600730173640575A0147C60070011FCA005000F7080385800CBEE006800A30C023520077A401840004BAC00D7A001FB31AAD10CC016923DA00686769E019DA780D0022394854167C2A56FB75200D33801F696D5B922F98B68B64E02460054CAE900949401BB80021D0562344E00042A16C6B8253000600B78020200E44386B068401E8391661C4E14B804D3B6B27CFE98E73BCF55B65762C402768803F09620419100661EC2A8CE0008741A83917CC024970D9E718DD341640259D80200008444D8F713C401D88310E2EC9F20F3330E059009118019A8803F12A0FC6E1006E3744183D27312200D4AC01693F5A131C93F5A131C970D6008867379CD3221289B13D402492EE377917CACEDB3695AD61C939C7C10082597E3740E857396499EA31980293F4FD206B40123CEE27CFB64D5E57B9ACC7F993D9495444001C998E66B50896B0B90050D34DF3295289128E73070E00A4E7A389224323005E801049351952694C000";
        binaryInput = "";
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '0':
                    binaryInput += "0000";
                    break;
                case '1':
                    binaryInput += "0001";
                    break;
                case '2':
                    binaryInput += "0010";
                    break;
                case '3':
                    binaryInput += "0011";
                    break;
                case '4':
                    binaryInput += "0100";
                    break;
                case '5':
                    binaryInput += "0101";
                    break;
                case '6':
                    binaryInput += "0110";
                    break;
                case '7':
                    binaryInput += "0111";
                    break;
                case '8':
                    binaryInput += "1000";
                    break;
                case '9':
                    binaryInput += "1001";
                    break;
                case 'A':
                    binaryInput += "1010";
                    break;
                case 'B':
                    binaryInput += "1011";
                    break;
                case 'C':
                    binaryInput += "1100";
                    break;
                case 'D':
                    binaryInput += "1101";
                    break;
                case 'E':
                    binaryInput += "1110";
                    break;
                case 'F':
                    binaryInput += "1111";
                    break;
                default:
                    System.exit(1);
            }
        }
    }

    public void solution() {
        counter = 0;
        Packet packet = packets();
        System.out.println("Part1: " + sumVersion(packet));
        System.out.println("Part2: " + evaluate(packet));
    }

    public int sumVersion(Packet packet) {
        int sum = packet.version;
        if (packet.innerPackets != null) {
            for (Packet p : packet.innerPackets) {
                sum += sumVersion(p);
            }
        }
        return sum;
    }

    public long evaluate(Packet p) {
        long result = p.value;
        List<Long> innerResult = new ArrayList<>();
        if (p.innerPackets != null) {
            for (Packet packet : p.innerPackets) {
                innerResult.add(evaluate(packet));
            }
        }
        if (p.typeID < 4) {
            //overwriting the 0 from p.value
            if (p.typeID == 0) {
                result = 0;
            } else if (p.typeID == 1) {
                result = 1;
            } else if (p.typeID == 2) {
                result = 999999999;
            }
            for (long number : innerResult) {
                if (p.typeID == 0) {
                    result += number;
                } else if (p.typeID == 1) {
                    result *= number;
                } else if (p.typeID == 2) {
                    result = Math.min(result, number);
                } else if (p.typeID == 3) {
                    result = Math.max(result, number);
                }
            }
        } else if (p.typeID == 5) {
            result = innerResult.get(0) > innerResult.get(1) ? 1 : 0;
        } else if (p.typeID == 6) {
            result = innerResult.get(0) < innerResult.get(1) ? 1 : 0;
        } else if (p.typeID == 7) {
            result = (long) innerResult.get(0) == (long) innerResult.get(1) ? 1 : 0;
        }
        return result;
    }

    public Packet packets() {
        Packet packet = newPacket();
        if (packet.typeID == 4) {
            packet.value = getNumber();
        } else {
            packet.operator = Integer.parseInt(String.valueOf(binaryInput.charAt(counter)));
            packet.innerPackets = new ArrayList<>();
            counter++;
            if (packet.operator == 1) {
                packet.label = Integer.parseInt(binaryInput.substring(counter, counter + 11), 2);
                counter += 11;
                for (int i = 0; i < packet.label; i++) {
                    packet.innerPackets.add(packets());
                }
            } else {
                packet.label = Integer.parseInt(binaryInput.substring(counter, counter + 15), 2);
                counter += 15;
                int currentCounter = counter;
                while (counter < currentCounter + packet.label) {
                    packet.innerPackets.add(packets());
                }
            }
        }
        return packet;
    }

    public Packet newPacket() {
        Packet packet = new Packet();
        packet.version = Integer.parseInt(binaryInput.substring(counter, counter + 3), 2);
        counter += 3;
        packet.typeID = Integer.parseInt(binaryInput.substring(counter, counter + 3), 2);
        counter += 3;
        return packet;
    }

    public long getNumber() {
        String number = "";
        boolean run;
        do {
            run = binaryInput.charAt(counter) == '1';
            counter++;
            number += binaryInput.substring(counter, counter + 4);
            counter += 4;
        } while (run);
        return Long.parseLong(number, 2);
    }
}
