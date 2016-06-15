package br.unicamp.ic.timeverde.domain.enumeration;

/**
 * The DeviceStatusEnum enumeration.
 */
public enum DeviceStatusEnum {
    OFF(0),
    ON(1);

    private int status;
        DeviceStatusEnum(int status) {
         this.status = status;
    }

        public int value(){
        return this.status;
    }
}
