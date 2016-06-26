package br.unicamp.ic.timeverde.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MacroScheduling.
 */
@Entity
@Table(name = "macro_scheduling")
public class MacroScheduling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "days_in_week")
    private String daysInWeek;

    @Column(name = "time")
    private String time;

    @ManyToOne
    private Macro macro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDaysInWeek() {
        return daysInWeek;
    }

    public void setDaysInWeek(String daysInWeek) {
        this.daysInWeek = daysInWeek;
    }

    public Macro getMacro() {
        return macro;
    }

    public void setMacro(Macro macro) {
        this.macro = macro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MacroScheduling macroScheduling = (MacroScheduling) o;
        if(macroScheduling.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, macroScheduling.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MacroScheduling{" +
            "id=" + id +
            ", daysInWeek='" + daysInWeek + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
